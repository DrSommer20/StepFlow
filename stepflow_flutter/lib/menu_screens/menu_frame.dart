import 'package:flutter/material.dart';
import 'package:ionicons/ionicons.dart';
import 'package:stepflow/menu_screens/events.dart';
import 'home.dart';
import 'fines.dart';
import 'carpool.dart';
import 'profile.dart';

class MenuFrame extends StatefulWidget {
  final VoidCallback onLogout;

  const MenuFrame({super.key, required this.onLogout});

  @override
  MenuFrameState createState() => MenuFrameState();
}

class MenuFrameState extends State<MenuFrame> {
  int _selectedIndex = 0;

  static final List<Widget> _widgetOptions = <Widget>[
    HomeScreen(),
    EventsNewScreen(),
    FinesScreen(),
    CarpoolScreen(),
    ProfileScreen(),
  ];

  void _onItemTapped(int index) {
    setState(() {
      _selectedIndex = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false, // Remove back button
        title: Text(
            _selectedIndex == 0 ? 'Home' :
            _selectedIndex == 1 ? 'Events' :
            _selectedIndex == 2 ? 'Fines' :
            _selectedIndex == 3 ? 'Carpool' :
            'Profile',
          style: const TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold), // Larger title
        ),
        centerTitle: true,  // Center the title if you want
        elevation: 5, 
        actions: [
          IconButton(
            icon: const Icon(Icons.logout),
            onPressed: widget.onLogout,
          ),
        ],
      ),
      body: Center(
        child: _widgetOptions.elementAt(_selectedIndex),
      ),
      bottomNavigationBar: NavigationBar(
        selectedIndex: _selectedIndex,
        labelBehavior: NavigationDestinationLabelBehavior.alwaysHide,
        height: 60,
        indicatorShape: CircleBorder(
          side: BorderSide(color: Colors.blue, width: 40),
        ),
        onDestinationSelected: _onItemTapped,
        destinations: const <NavigationDestination>[
          NavigationDestination(
        icon: Icon(Ionicons.home),
        label: 'Home',
          ),
          NavigationDestination(
        icon: Icon(Ionicons.calendar),
        label: 'Events',
          ),
          NavigationDestination(
        icon: Icon(Ionicons.cash),
        label: 'Fines',
          ),
          NavigationDestination(
        icon: Icon(Ionicons.car),
        label: 'Carpool',
          ),
          NavigationDestination(
        icon: Icon(Ionicons.person),
        label: 'Profile',
          ),
        ],
      ),);
  }
}