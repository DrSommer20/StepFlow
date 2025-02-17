import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'menu_screens/menu_frame.dart';
import 'registration/register_screen.dart'; // Import the new register screen

class LoginScreen extends StatefulWidget {
  final VoidCallback onLoginSuccess;

  const LoginScreen({super.key, required this.onLoginSuccess});

  @override
  State<LoginScreen> createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Sign In'),
        centerTitle: true,
        backgroundColor: Colors.lightBlueAccent,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: SingleChildScrollView(
          child: Column(
            mainAxisAlignment: MainAxisAlignment.center,
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              SizedBox(height: 32.0),
              Text(
                'Welcome Back',
                textAlign: TextAlign.left,
                style: TextStyle(
                  fontSize: 34,
                  fontWeight: FontWeight.bold,
                  color: Colors.blue[900],
                ),
              ),
              SizedBox(height: 20),
              Text(
                'Please enter your email address and password for login',
                textAlign: TextAlign.left,
                style: TextStyle(
                  fontSize: 16,
                  color: Colors.grey,
                ),
              ),
                SizedBox(height: 40),
                TextField(
                controller: _emailController,
                decoration: InputDecoration(
                  labelText: 'Email',
                  hintText: 'mustermann@gmail.com',
                  border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20.0),
                  ),
                  focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20.0),
                  borderSide: BorderSide(
                    color: Colors.lightBlueAccent,
                    width: 2.0,
                  ),
                  ),
                  contentPadding: EdgeInsets.symmetric(vertical: 25.0, horizontal: 25.0),
                ),
                keyboardType: TextInputType.emailAddress,
                style: TextStyle(fontSize: 20.0), // Increase font size
                ),
              SizedBox(height: 16.0),
                TextField(
                controller: _passwordController,
                decoration: InputDecoration(
                  labelText: 'Password',
                  hintText: 'Enter your password',
                  border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20.0),
                  ),
                  focusedBorder: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(20.0),
                  borderSide: BorderSide(
                    color: Colors.lightBlueAccent,
                    width: 2.0,
                  ),
                  ),
                  contentPadding: EdgeInsets.symmetric(vertical: 25.0, horizontal: 25.0), // Increase padding
                ),
                keyboardType: TextInputType.visiblePassword,
                style: TextStyle(fontSize: 20.0), // Decrease font size
                obscureText: true,
                ),
              SizedBox(height: 16.0),
              Align(
                alignment: Alignment.centerRight,
                child: TextButton(
                  onPressed: () {
                    Navigator.pushReplacement(
                      context,
                      MaterialPageRoute(
                          builder: (context) => MenuFrame()),
                    );
                  },
                  child: Text(
                    'Forgot Password?',
                    style: TextStyle(color: Colors.blue),
                  ),
                ),
              ),
              SizedBox(height: 16.0),
              Center(
                child: ElevatedButton(
                  onPressed: () async {
                    try {
                      final loginResponse = await sendLogin(
                          _emailController.text, _passwordController.text);

                      if (loginResponse.statusCode == 200) {
                        final loginData = jsonDecode(loginResponse.body);
                        final token = loginData['token']; // Access token
                        
                        final prefs = await SharedPreferences.getInstance();
                        await prefs.setString('token', token);
                        widget.onLoginSuccess();

                        if (!context.mounted) return;
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(
                              builder: (context) => MenuFrame()),
                        );
                      } else if (loginResponse.statusCode == 401) {
                        // Handle invalid credentials
                        if (!context.mounted) return;
                        ScaffoldMessenger.of(context).showSnackBar(
                          const SnackBar(
                              content: Text('Invalid email or password.')),
                        );
                      } else {
                        if (!context.mounted) return;
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                              content: Text(
                                  'Login failed: ${loginResponse.statusCode}')),
                        );
                      }
                    } catch (e) {
                      if (!context.mounted) return;
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('An error occurred.')),
                      );
                    }
                  },
                  style: ElevatedButton.styleFrom(
                    backgroundColor: Colors.blue[900],
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(20),
                    ),
                    padding: EdgeInsets.symmetric(
                      horizontal: 150.0,
                      vertical: 30.0,
                    ),
                    shadowColor: Colors.blueAccent,
                    elevation: 5.0,
                  ),
                  child: Text(
                    'Sign In',
                    style: TextStyle(
                      fontSize: 18,
                      color: Colors.white,
                    ),
                  ),
                ),
              ),
              SizedBox(height: 20.0),
              Center(
                child: Text(
                  'Sign in with',
                  style: TextStyle(color: Colors.grey),
                ),
              ),
              SizedBox(height: 20.0),
              Center(
                child: Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    IconButton(
                      icon: Icon(Icons.apple),
                      onPressed: () {
                        // Handle Apple sign in
                      },
                    ),
                    SizedBox(width: 20.0),
                    IconButton(
                      icon: Icon(Icons.g_mobiledata),
                      onPressed: () {
                        // Handle Google sign in
                      },
                    ),
                  ],
                ),
              ),
              SizedBox(height: 20.0),
              Center(
                child: TextButton(
                  onPressed: () {
                    Navigator.push(
                      context,
                      MaterialPageRoute(builder: (context) => RegisterScreen()), // Navigate to the register screen
                    );
                  },
                  child: Text.rich(
                    TextSpan(
                      text: 'Not Registered Yet? ',
                      style: TextStyle(color: Colors.grey),
                      children: [
                        TextSpan(
                          text: 'Sign Up',
                          style: TextStyle(color: Colors.blue),
                        ),
                      ],
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Future<http.Response> sendLogin(String email, String password) async {
    try {
      final response = await http.post(
        Uri.parse('http://192.168.0.136:8080/api/auth/login'),
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: jsonEncode({
          'email': email,
          'password': password,
        }),
      );
      return response;
    } catch (e) {
      throw Exception('Failed to connect to the server: $e');
    }
  }

  Future<String?> refreshAccessToken() async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('token');

    if (token == null) {
      return null; // No refresh token available
    }

    try {
      final response = await http.post(
        Uri.parse('http://192.168.0.136:8080/api/auth/refresh_token'), // Refresh endpoint
        headers: {'Content-Type': 'application/json'},
        body: jsonEncode({'token': token}), // Send refresh token
      );

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        final newAccessToken = data['token'];
        await prefs.setString('token', newAccessToken); // Store new access token
        return newAccessToken;
      } else {
        // Handle refresh token failure (e.g., clear tokens and redirect to login)
        await prefs.remove('token');
        return null; // Indicate failure
      }
    } catch (e) {
      return null;
    }
  }
}