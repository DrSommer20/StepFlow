import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:ionicons/ionicons.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;
import 'menu_screens/menu_frame.dart';
import 'login_screen.dart';


// Approximate Color Values (Adjust these based on your image)
const Color primaryColor = Color(0xFF005CB2); // Darkest Blue
const Color secondaryColor = Color(0xFFF26A25); // Darkest Orange
const Color surfaceColor = Color(0xFFE0F2F7); // Lightest Blue
const Color errorColor = Colors.red; // Standard Red (Customize if needed)
const Color backgroundColor = Colors.white; // White Background

// Define the Color Scheme
ColorScheme colorScheme = ColorScheme.light(
  primary: primaryColor,
  secondary: secondaryColor,
  surface: surfaceColor,
  error: errorColor,  
  onPrimary: Colors.white, // White text on Primary Blue
  onSecondary: Colors.white, // White text on Secondary Orange
  onSurface: Colors.black, // Black text on Lightest Blue
  onError: Colors.white,  // White text on Error Red
);

// Create the ThemeData
ThemeData lightTheme = ThemeData(
  colorScheme: colorScheme,
  fontFamily: 'Roboto', // Or your preferred font family
  textTheme: const TextTheme(
    headlineLarge: TextStyle(fontSize: 32.0, fontWeight: FontWeight.bold),
    headlineMedium: TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold),
    bodyLarge: TextStyle(fontSize: 16.0),
    bodyMedium: TextStyle(fontSize: 14.0),
  ),
  // Add other theme customizations as needed (e.g., AppBar theme)
  appBarTheme: const AppBarTheme(
    backgroundColor: primaryColor, // Primary Blue AppBar
    foregroundColor: Colors.white, // White text on AppBar
  ),
  elevatedButtonTheme: ElevatedButtonThemeData(
    style: ElevatedButton.styleFrom(
      backgroundColor: secondaryColor, // Secondary Orange Button
      foregroundColor: Colors.white, // White text on Button
    ),
  ),
);

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  Icon(Ionicons.add);
  Icon(Ionicons.add_outline);
  Icon(Ionicons.add_sharp);

  runApp(MyApp());
}

class MyApp extends StatefulWidget {

  const MyApp({super.key});

  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  bool _isLoggedIn = false;

  @override
  void initState() {
    super.initState();
    _checkToken();
  }

  Future<void> _checkToken() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('token');
    if (token == null) {
      setState(() {
        _isLoggedIn = false;
      });
      return;
    }
    final response = await http.get(
      Uri.parse('http://192.168.0.136:8080/api/auth/validate'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );
    if (response.statusCode == 200) {
      final userResponse = await http.get(
        Uri.parse('http://192.168.0.136:8080/api/users'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );
      if (userResponse.statusCode == 200) {
        final userData = jsonDecode(userResponse.body);
        final List<int> teamIds = List<int>.from(userData['teamIds']);
        final List<int> teamAdmins = List<int>.from(userData['admins']);
        if (teamIds.isEmpty) {
          setState(() {
            _isLoggedIn = false;
          });
          return;
        }
        await prefs.setInt('userId', userData['userId']);
        await prefs.setString('name', userData['name']);
        await prefs.setString('firstName', userData['firstName']);
        await prefs.setString('email', userData['email']);
        await prefs.setString('password', userData['password']);
        await prefs.setBool('active', userData['active']);
        await prefs.setStringList('teamIds', teamIds.map((id) => id.toString()).toList());
        await prefs.setInt('currentTeamId', teamIds[0]);
        await prefs.setBool('admin', teamAdmins.contains(userData['userId']));
        setState(() {
          _isLoggedIn = true;
        });
        return;
      }
    }
    setState(() {
      _isLoggedIn = false;
    });
  }

  Future<void> _logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('token');
    setState(() {
      _isLoggedIn = false;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'StepFlow',
      theme: lightTheme,
      themeMode: ThemeMode.system,
      home: _isLoggedIn ? MenuFrame(onLogout: _logout) : LoginScreen(onLoginSuccess: _checkToken),
    );
  }
}