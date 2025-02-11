import 'dart:io';

import 'package:flutter/material.dart';
import 'package:image_picker/image_picker.dart';
import 'package:flutter_colorpicker/flutter_colorpicker.dart';

class CreateTeamScreen extends StatefulWidget {
  const CreateTeamScreen({super.key});

  @override
  CreateTeamScreenState createState() => CreateTeamScreenState();
}

class CreateTeamScreenState extends State<CreateTeamScreen> {
  final TextEditingController _teamNameController = TextEditingController();
  final TextEditingController _teamDescriptionController = TextEditingController();
  Color _teamColor = Colors.blue;
  XFile? _teamLogo;
  XFile? _teamBanner;

  Future<void> _pickImage(ImageSource source, bool isLogo) async {
    final ImagePicker picker = ImagePicker();
    final XFile? image = await picker.pickImage(source: source);
    setState(() {
      if (isLogo) {
        _teamLogo = image;
      } else {
        _teamBanner = image;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Stack(
          children: [
            // Oberer Bereich mit blauem Farbverlauf
            Container(
              height: MediaQuery.of(context).size.height * 0.4, // Adjusted height
              decoration: const BoxDecoration(
                gradient: LinearGradient(
                  colors: [Colors.lightBlueAccent, Colors.blue],
                  begin: Alignment.topCenter,
                  end: Alignment.bottomCenter,
                ),
              ),
              child: const Center(
                child: Icon(
                  Icons.group,
                  size: 80, // Adjusted size
                  color: Colors.white,
                ),
              ),
            ),
            // Unterer Bereich mit weißer Karte
            Align(
              alignment: Alignment.bottomCenter,
              child: Container(
                height: MediaQuery.of(context).size.height * 0.6, // Adjusted height
                padding: const EdgeInsets.only(top: 20), // Added padding to avoid overlap
                decoration: const BoxDecoration(
                  color: Colors.white,
                  borderRadius: BorderRadius.only(
                    topLeft: Radius.circular(30),
                    topRight: Radius.circular(30),
                  ),
                  boxShadow: [
                    BoxShadow(
                      color: Colors.black12,
                      blurRadius: 10,
                      spreadRadius: 5,
                    ),
                  ],
                ),
                child: Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: Column(
                    mainAxisSize: MainAxisSize.min, // Important for dynamic content
                    children: [
                      Text(
                        'Create a Team',
                        textAlign: TextAlign.left,
                        style: TextStyle(
                          fontSize: 24, // Adjusted size
                          fontWeight: FontWeight.bold,
                          color: Colors.blue[900],
                        ),
                      ),
                      const SizedBox(height: 16),
                      TextField(
                        controller: _teamNameController,
                        decoration: const InputDecoration(
                          labelText: 'Team Name',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 16),
                      TextField(
                        controller: _teamDescriptionController,
                        decoration: const InputDecoration(
                          labelText: 'Team Description',
                          border: OutlineInputBorder(),
                        ),
                      ),
                      const SizedBox(height: 16),
                      // ... (rest of the code for color picker and image upload)

                       Row(
                        children: [
                          const Text('Team Color:'),
                          const SizedBox(width: 8),
                          GestureDetector(
                            onTap: () async {
                              Color? pickedColor = await showDialog(
                                context: context,
                                builder: (context) => AlertDialog(
                                  title: const Text('Pick a color'),
                                  content: ColorPicker(
                                    pickerColor: _teamColor,
                                    onColorChanged: (color) {
                                      setState(() {
                                        _teamColor = color;
                                      });
                                    },
                                  ),
                                  actions: <Widget>[
                                    TextButton(
                                      child: const Text('Done'),
                                      onPressed: () {
                                        Navigator.of(context).pop();
                                      },
                                    ),
                                  ],
                                ),
                              );
                              if (pickedColor != null) {
                                setState(() {
                                  _teamColor = pickedColor;
                                });
                              }
                            },
                            child: Container(
                              width: 24,
                              height: 24,
                              decoration: BoxDecoration(
                                color: _teamColor,
                                shape: BoxShape.circle,
                              ),
                            ),
                          ),
                        ],
                      ),
                      const SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: () => _pickImage(ImageSource.gallery, true),
                        child: const Text('Upload Team Logo'),
                      ),
                      if (_teamLogo != null)
                        Image.file(File(_teamLogo!.path), height: 100), // Added height
                      const SizedBox(height: 16),
                      ElevatedButton(
                        onPressed: () => _pickImage(ImageSource.gallery, false),
                        child: const Text('Upload Team Banner'),
                      ),
                      if (_teamBanner != null)
                        Image.file(File(_teamBanner!.path), height: 100),  // Added height
                      const SizedBox(height: 32),
                      ElevatedButton(
                        onPressed: () {
                          // Handle create team logic here
                        },
                        style: ElevatedButton.styleFrom(
                          backgroundColor: Colors.blue[900],
                          shape: RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(30),
                          ),
                          padding: const EdgeInsets.symmetric(
                            horizontal: 40,
                            vertical: 15,
                          ),
                        ),
                        child: const Text(
                          'Create Team',
                          style: TextStyle(
                            fontSize: 18,
                            color: Colors.white,
                          ),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
    );
  }
}