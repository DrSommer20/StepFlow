import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;
import 'menu_screens/menu_frame.dart';
import 'login_screen.dart';
import 'dart:convert';
import 'package:flutter/services.dart';
import 'package:json_theme/json_theme.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  final themeStr = await rootBundle.loadString('assets/appainter_theme.json');
  final themeJson = jsonDecode(themeStr);
  final theme = ThemeDecoder.decodeThemeData(themeJson)!;

  final darkThemeStr = await rootBundle.loadString('assets/appainter_theme_dark_mode.json');
  final darkThemeJson = jsonDecode(darkThemeStr);
  final darkTheme = ThemeDecoder.decodeThemeData(darkThemeJson) ?? ThemeData.dark();

  runApp(MyApp(theme: theme, darkTheme: darkTheme));
}

class MyApp extends StatelessWidget {
  final ThemeData theme;
  final ThemeData darkTheme;

  const MyApp({super.key, required this.theme, required this.darkTheme});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'StepFlow',
      theme: theme,
      darkTheme: darkTheme,
      themeMode: ThemeMode.system,
      home: FutureBuilder(
        future: _checkToken(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return CircularProgressIndicator();
          } else if (snapshot.hasData && snapshot.data == true) {
            return MenuFrame();
          } else {
            return LoginScreen();
          }
        },
      ),
    );
  }

  Future<bool> _checkToken() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('auth_token');
    if (token == null) {
      return false;
    }
    final response = await http.get(
      Uri.parse('http://192.168.0.136:8080/api/auth/validate'),
      headers: {
        'Authorization': 'Bearer $token',
      },
    );
    if (response.statusCode == 200) {
      return true;
    } else {
      return false;
    }
  }
}