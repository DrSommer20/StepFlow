import 'package:flutter/material.dart';

class FinesScreen extends StatelessWidget {
  const FinesScreen({super.key});

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Fines Screen'),
      ),
      body: Center(
        child: Text('Welcome to the Fines Screen!'),
      ),
    );
  }
}
