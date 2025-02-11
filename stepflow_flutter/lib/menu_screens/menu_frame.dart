import 'package:flutter/material.dart';
import 'package:ionicons/ionicons.dart';
import 'package:stepflow/menu_screens/events_new.dart';
import 'home.dart';
import 'fines.dart';
import 'carpool.dart';
import 'profile.dart';

class MenuFrame extends StatefulWidget {
  const MenuFrame({super.key});

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