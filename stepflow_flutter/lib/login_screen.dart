import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';
import 'menu_screens/menu_frame.dart';

class LoginScreen extends StatelessWidget {
  final TextEditingController _emailController = TextEditingController();
  final TextEditingController _passwordController = TextEditingController();

  LoginScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Login'),
        centerTitle: true,
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Center(
          child: SingleChildScrollView(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                Image.asset('assets/StepFlow.jpg', height: 100),
                SizedBox(height: 32.0),
                TextField(
                  controller: _emailController,
                  decoration: InputDecoration(
                    labelText: 'Email',
                    border: OutlineInputBorder(),
                  ),
                  keyboardType: TextInputType.emailAddress,
                ),
                SizedBox(height: 16.0),
                TextField(
                  controller: _passwordController,
                  decoration: InputDecoration(
                    labelText: 'Password',
                    border: OutlineInputBorder(),
                  ),
                  obscureText: true,
                ),
                SizedBox(height: 16.0),
                ElevatedButton(
                  onPressed: () async {
                    try {
                      final loginResponse = await sendLogin(
                          _emailController.text, _passwordController.text);

                      if (loginResponse.statusCode == 200) {
                        final loginData = jsonDecode(loginResponse.body);
                        final token = loginData['token']; // Access token
                        print('Access token: $token');
                        print(loginData);

                        final prefs = await SharedPreferences.getInstance();
                        await prefs.setString('token', token);

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
                        print('Login failed: ${loginResponse.body}');
                        if (!context.mounted) return;
                        ScaffoldMessenger.of(context).showSnackBar(
                          SnackBar(
                              content: Text(
                                  'Login failed: ${loginResponse.statusCode}')),
                        );
                      }
                    } catch (e) {
                      print('Error: $e');
                      if (!context.mounted) return;
                      ScaffoldMessenger.of(context).showSnackBar(
                        SnackBar(content: Text('An error occurred.')),
                      );
                    }
                  },
                  child: Text('Login'),
                ),
                SizedBox(height: 16.0),
                TextButton(
                  onPressed: () {
                    print('Register as Teamlead');
                  },
                  child: Text('Register'),
                ),
                TextButton(
                  onPressed: () {
                   print('Forgot Password?');
                  },
                  child: Text('Forgot Password?'),
                ),
              ],
            ),
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
        print('Token refresh failed: ${response.statusCode}, ${response.body}');
        // Handle refresh token failure (e.g., clear tokens and redirect to login)
        await prefs.remove('token');
        return null; // Indicate failure
      }
    } catch (e) {
      print('Error during token refresh: $e');
      return null;
    }
  }
}