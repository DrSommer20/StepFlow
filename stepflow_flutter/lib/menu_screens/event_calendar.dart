import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';
import 'events.dart'; // Import your EventDTO

class EventDataSource extends CalendarDataSource {
  EventDataSource(List<EventDTO> appointments) {
    _appointments = appointments;
  }

  final Map<String, Color> namedColors = {
    'blue': Colors.blue,
    'red': Colors.red,
    'green': Colors.green,
    'yellow': Colors.yellow,
    'purple': Colors.purple,
    // Add more named colors as needed
  };

  @override
  List<EventDTO> get appointments => _appointments;

  List<EventDTO> _appointments = <EventDTO>[];

  @override
  DateTime getStartTime(int index) {
    return _appointments[index].start;
  }

  @override
  DateTime getEndTime(int index) {
    return _appointments[index].end;
  }

  @override
  String getSubject(int index) {
    return _appointments[index].title;
  }
  
  String? getStartTimeText(int index) {
    if (isAllDay(index)) {
      return 'All Day'; // Display "All Day" for all-day events
    } else {
      return DateFormat.jm().format(_appointments[index].start); // Format time
    }
  }

  String? getEndTimeText(int index) {
    if (isAllDay(index)) {
      return 'All Day'; // Display "All Day" for all-day events
    } else {
      return DateFormat.jm().format(_appointments[index].end); // Format time
    }
  }

    @override
  Color getColor(int index) {
    final colorString = _appointments[index].color;
    if (colorString != null) {
      if (namedColors.containsKey(colorString.toLowerCase())) {
        return namedColors[colorString.toLowerCase()]!;
      }
      try {
        return Color(int.parse(colorString.replaceAll("#", "0xff")));
      } catch (e) {
        print("Error parsing color: $e, colorString: $colorString");
        return Colors.blue; // Default color on error
      }
    }
    return Colors.blue;
  }

  @override
  bool isAllDay(int index) {
    return _appointments[index].allDay?? false; // Provide a default value
  }

  @override
  String? getRecurrenceRule(int index) {
    return _appointments[index].recurrenceRule;
  }
}