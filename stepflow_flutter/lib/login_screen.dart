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
                      final response = await sendLogin(_emailController.text, _passwordController.text);
                      if (response.statusCode == 200) {
                        final responseData = jsonDecode(response.body);
                        final token = responseData['token'];
                        final prefs = await SharedPreferences.getInstance();
                        await prefs.setString('auth_token', token);
                        Navigator.pushReplacement(
                          context,
                          MaterialPageRoute(builder: (context) => MenuFrame()),
                        );
                      } else {
                        print('Login failed: ${response.body}');
                      }
                    } catch (e) {
                      print('Error: $e');
                    }
                  },
                  child: Text('Login'),
                ),
                SizedBox(height: 16.0),
                TextButton(
                  onPressed: () {
                    // Handle forgot password logic here
                    print('Forgot Password pressed');
                  },
                  child: Text('Forgot Password?'),
                ),
                TextButton(
                  onPressed: () {
                    // Navigate to the registration screen
                    print('Register pressed');
                  },
                  child: Text("Don't have an account? Register"),
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
}