import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:stepflow/menu_screens/event_dto.dart';

class EventDetailsScreen extends StatelessWidget {
  final EventDTO event;
  final bool userRole;

  const EventDetailsScreen({super.key, required this.event, required this.userRole});

  

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(event.title),
        actions: [
          if (userRole)
            IconButton(
              icon: const Icon(Icons.edit),
              onPressed: () {
                // Navigate to edit event screen
                Navigator.push(
                  context,
                  MaterialPageRoute(
                    builder: (context) => EditEventScreen(event: event),
                  ),
                );
              },
            ),
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Text(
              event.title,
              style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 16),
            Text(
              'Description: ${event.description}',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Text(
              'Location: ${event.location ?? 'N/A'}',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Text(
              'Start: ${DateFormat.yMMMd().add_jm().format(event.start)}',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Text(
              'End: ${DateFormat.yMMMd().add_jm().format(event.end)}',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Text(
              'All Day: ${event.allDay == true ? 'Yes' : 'No'}',
              style: const TextStyle(fontSize: 16),
            ),
            const SizedBox(height: 16),
            Text(
              'Color: ${event.color ?? 'N/A'}',
              style: const TextStyle(fontSize: 16),
            ),
          ],
        ),
      ),
    );
  }
}

class EditEventScreen extends StatelessWidget {
  final EventDTO event;

  const EditEventScreen({super.key, required this.event});

  @override
  Widget build(BuildContext context) {
    // Implement the edit event screen here
    return Scaffold(
      appBar: AppBar(
        title: const Text('Edit Event'),
      ),
      body: Center(
        child: Text('Edit Event Screen for ${event.title}'),
      ),
    );
  }
}