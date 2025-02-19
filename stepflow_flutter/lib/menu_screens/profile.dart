import 'package:flutter/material.dart';
import 'package:intl/date_symbol_data_local.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:http/http.dart' as http;
import 'dart:convert';
import 'package:ionicons/ionicons.dart';

class ProfileScreen extends StatefulWidget {
  const ProfileScreen({super.key});

  @override
  _ProfileScreenState createState() => _ProfileScreenState();
}

class _ProfileScreenState extends State<ProfileScreen> {
  Future<Map<String, dynamic>> _getUserData() async {
    final prefs = await SharedPreferences.getInstance();
    final userId = prefs.getInt('userId');
    final name = prefs.getString('name');
    final firstName = prefs.getString('firstName');
    final email = prefs.getString('email');
    final password = prefs.getString('password');
    final active = prefs.getBool('active');
    final teamIds = prefs.getStringList('teamIds');

    return {
      'userId': userId,
      'name': name,
      'firstName': firstName,
      'email': email,
      'password': password,
      'active': active,
      'teamIds': teamIds,
    };
  }

  Future<Map<int, String>> _getTeamNames(List<String> teamIds) async {
    final prefs = await SharedPreferences.getInstance();
    final token = prefs.getString('token');
    final Map<int, String> teamNames = {};
    for (String id in teamIds) {
      final response = await http.get(
        Uri.parse('http://192.168.0.136:8080/api/teams/$id'),
        headers: {
          'Authorization': 'Bearer $token',
        },
      );
      if (response.statusCode == 200) {
        final teamData = jsonDecode(response.body);
        teamNames[int.parse(id)] = teamData['teamName'];
      }
    }
    return teamNames;
  }

  @override
  Widget build(BuildContext context) {
    return FutureBuilder(
      future: Future.wait([
        initializeDateFormatting('de_DE', null), // Initialize for German locale
        _getUserData(),
      ]),
      builder: (context, snapshot) {
        if (snapshot.connectionState == ConnectionState.waiting) {
          return const Center(child: CircularProgressIndicator()); // Show loading indicator
        } else if (snapshot.hasError) {
          return Center(child: Text('Error: ${snapshot.error}')); // Handle errors
        } else {
          final userData = snapshot.data![1] as Map<String, dynamic>;
          final name = userData['name'] ?? '';
          final firstName = userData['firstName'] ?? '';
          final email = userData['email'] ?? '';
          final teamIds = userData['teamIds'] ?? [];
          final bio = 'This is a placeholder bio'; // Replace with actual bio if available

          return FutureBuilder(
            future: _getTeamNames(teamIds),
            builder: (context, teamSnapshot) {
              if (teamSnapshot.connectionState == ConnectionState.waiting) {
                return const Center(child: CircularProgressIndicator()); // Show loading indicator
              } else if (teamSnapshot.hasError) {
                return Center(child: Text('Error: ${teamSnapshot.error}')); // Handle errors
              } else {
                final teamNames = teamSnapshot.data as Map<int, String>;
                final teamItems = teamNames.entries.map((entry) {
                  return DropdownMenuItem<int>(
                    value: entry.key,
                    child: Text(entry.value),
                  );
                }).toList();

                return Scaffold(
                  body: SingleChildScrollView(
                    padding: const EdgeInsets.all(16.0),

                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Center(
                              child: CircleAvatar(
                                radius: 60,
                                backgroundImage: const NetworkImage(
                                    'https://www.pngitem.com/middle/ixJxmw_profile-pic-dummy-png-transparent-png/'), // Replace with actual image URL
                              ),
                            ),
                            const SizedBox(height: 20),
                            Center(
                              // Center the name
                              child: Text(
                                '$firstName $name',
                                style: const TextStyle(
                                  fontSize: 24,
                                  fontWeight: FontWeight.bold,
                                ),
                              ),
                            ),
                            const SizedBox(height: 10),
                            Center(
                              // Center the email
                              child: Text(
                                email,
                                style: const TextStyle(fontSize: 16),
                              ),
                            ),
                            const SizedBox(height: 20),
                            const Text(
                              'Bio:',
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            Text(
                              bio,
                              style: const TextStyle(fontSize: 16),
                            ),
                            const SizedBox(height: 20),
                            const Text(
                              'Selected Team:',
                              style: TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.bold,
                              ),
                            ),
                            FutureBuilder<int?>(
                              future: SharedPreferences.getInstance().then((prefs) => prefs.getInt('currentTeamId')),
                              builder: (context, currentTeamSnapshot) {
                                if (currentTeamSnapshot.connectionState == ConnectionState.waiting) {
                                  return const CircularProgressIndicator();
                                } else {
                                  final currentTeamId = currentTeamSnapshot.data;
                                  return Column(
                                    crossAxisAlignment: CrossAxisAlignment.start,
                                    children: [
                                      MenuAnchor(
                                        builder: (context, controller, child) {
                                          return ElevatedButton(
                                            onPressed: () {
                                              controller.open();
                                            },
                                            style: ElevatedButton.styleFrom(
                                              backgroundColor: Colors.blue[900],
                                              shape: RoundedRectangleBorder(
                                                borderRadius: BorderRadius.circular(20),
                                              ),
                                              padding: const EdgeInsets.symmetric(
                                                horizontal: 30.0,
                                                vertical: 15.0,
                                              ),
                                              shadowColor: Colors.blueAccent,
                                              // elevation: 5.0,
                                            ),
                                            child: Row(
                                              mainAxisSize: MainAxisSize.min,
                                              children: [
                                              Text(
                                                currentTeamId != null
                                                  ? teamNames[currentTeamId] ?? 'Select a team'
                                                  : 'Select a team',
                                                style: const TextStyle(
                                                fontSize: 16,
                                                color: Colors.white,
                                                ),
                                              ),
                                              Icon(Ionicons.caret_down, color: Colors.white),
                                              ],
                                            ),
                                          );
                                        },
                                        menuChildren: teamItems.map((item) {
                                          return ListTile(
                                            title: item.child,
                                            onTap: () async {
                                              final prefs = await SharedPreferences.getInstance();
                                              await prefs.setInt('currentTeamId', item.value!);
                                              setState(() {}); // Refresh the UI
                                            },
                                          );
                                        }).toList(),
                                      ),
                                      const SizedBox(height: 100),
                                      
                                    ],
                                  );
                                }
                              },
                            ),
                          ],
                        ),
                        
                      ],
                    ),
                  ),
                );
              }
            },
          );
        }
      },
    );
  }
}