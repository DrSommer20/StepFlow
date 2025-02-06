import 'dart:convert';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:intl/intl.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:stepflow/menu_screens/event_calendar.dart';
import 'package:syncfusion_flutter_calendar/calendar.dart';
import 'package:stepflow/login_screen.dart';

class EventDTO {
  int? id;
  String title;
  String description;
  DateTime start;
  DateTime end;
  String? location;
  List<String>? attendees;
  String? color;
  bool? recurrent;
  String? recurrenceRule;
  bool? allDay;

  EventDTO({
    this.id,
    required this.title,
    required this.description,
    required this.start,
    required this.end,
    this.location,
    this.attendees,
    this.color,
    this.recurrent,
    this.recurrenceRule,
    this.allDay,
  });

  factory EventDTO.fromJson(Map<String, dynamic> json) {
    return EventDTO(
        id: json['id'],
        title: json['title'],
        description: json['description'] ?? '',
        start: DateTime.parse(json['start']),
        end: DateTime.parse(json['end']),
        location: json['location'],
        attendees: json['attendees'] != null
            ? List<String>.from(json['attendees'])
            : null,
        color: json['color'],
        recurrent: json['recurrent'] != null
            ? json['recurrent'].toString().toLowerCase() == 'true'
            : null,
        recurrenceRule: json['recurrenceRule'],
        allDay: json['allDay']);
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'start': start.toIso8601String(),
      'end': end.toIso8601String(),
      'location': location,
      'attendees': attendees,
      'color': color,
      'recurrent': recurrent?.toString(),
      'recurrenceRule': recurrenceRule,
      'allDay': allDay,
    };
  }
}

class EventsScreen extends StatefulWidget {
  const EventsScreen({super.key});

  @override
  State<EventsScreen> createState() => _EventsScreenState();
}

class _EventsScreenState extends State<EventsScreen> {
  final CalendarController _calendarController =
      CalendarController(); // Create controller

  List<EventDTO> events = [];
  final String apiUrl = 'http://192.168.0.136:8080/api/events';
  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _allDay = false;
  DateTime _startDateTime = DateTime.now();
  DateTime _endDateTime = DateTime.now().add(const Duration(hours: 1));
  String? _authToken;
  String? _selectedColor;
  bool _isRecurrent = false;
  String? _selectedRecurrenceRule;

  final List<String> _recurrenceRules = [
    'DAILY',
    'WEEKLY',
    'MONTHLY',
    'YEARLY',
  ];

  @override
  void initState() {
    super.initState();
    _calendarController.view = CalendarView.month;
    _loadTokenAndEvents();
  }

  Future<void> _loadTokenAndEvents() async {
    final prefs = await SharedPreferences.getInstance();
    _authToken = prefs.getString('token');
    if (_authToken != null) {
      _loadEvents();
    } else {
      if (mounted) {
        Navigator.push(
          // Use Navigator.push for navigation
          context,
          MaterialPageRoute(builder: (context) => LoginScreen()),
        );
      }
    }
  }

  Future<void> _loadEvents() async {
    final response = await http.get(
      Uri.parse(apiUrl),
      headers: {'Authorization': 'Bearer $_authToken'},
    );

    if (response.statusCode == 200) {
      final jsonData = jsonDecode(response.body);

      if (jsonData is Map<String, dynamic> && jsonData.containsKey('events')) {
        final eventsData = jsonData['events'] as List<dynamic>;
        events = eventsData
            .map((eventJson) => EventDTO.fromJson(eventJson))
            .toList();
        setState(() {}); // Important: Call setState to rebuild the calendar
      } else {
        print('Error: Unexpected API response format: $jsonData');
        ScaffoldMessenger.of(context).showSnackBar(
          const SnackBar(content: Text('Error loading events. Check console.')),
        );
      }
    } else {
      print(
          'Error: Failed to load events: ${response.statusCode}, ${response.body}');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
            content: Text('Failed to load events: ${response.statusCode}')),
      );
    }
  }

  Future<void> _addEvent() async {
    if (_authToken == null) return;

    final event = EventDTO(
      title: _titleController.text,
      description: _descriptionController.text,
      start: _startDateTime,
      end: _endDateTime,
      color: _selectedColor, 
      recurrent: _isRecurrent, 
      recurrenceRule: _selectedRecurrenceRule, 
      allDay: _allDay, 
    );

    final response = await http.post(
      Uri.parse(apiUrl),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $_authToken',
      },
      body: jsonEncode(event.toJson()),
    );

    if (response.statusCode == 200) {
      _loadEvents();
      _titleController.clear();
      _descriptionController.clear();
      _selectedColor = null; // Zurücksetzen
      _isRecurrent = false; // Zurücksetzen
      _selectedRecurrenceRule = null; // Zurücksetzen
    } else {
      print('Failed to add event: ${response.statusCode}, ${response.body}');
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Failed to add event: ${response.statusCode}')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        actions: [
          Row(
              mainAxisAlignment:
                  MainAxisAlignment.spaceAround, // Distribute chips evenly
              children: <Widget>[
                _buildViewChip(CalendarView.day, 'Day', Icons.calendar_view_day),
                _buildViewChip(CalendarView.week, 'Week', Icons.calendar_view_week),
                _buildViewChip(CalendarView.month, 'Month',Icons.calendar_month),
                _buildViewChip(CalendarView.schedule, 'Schedule', Icons.list),
              ],
            ),
        ],
      ),
      body: SfCalendar(
        controller: _calendarController,
        dataSource: EventDataSource(events),
        monthViewSettings: const MonthViewSettings(showAgenda: true),
        firstDayOfWeek: 1,
        scheduleViewSettings: const ScheduleViewSettings(
            // Customize schedule view if needed
            ),
        onTap: (CalendarTapDetails details) {
          if (details.targetElement == CalendarElement.appointment ||
              details.targetElement == CalendarElement.agenda) {
            final EventDTO? tappedEvent =
                details.appointments?.first as EventDTO?; // Safely cast

            if (tappedEvent != null) {
              _showEventDetailsDialog(context, tappedEvent);
            }
          }
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: () {
          _showAddEventDialog(context);
        },
        child: const Icon(Icons.add),
      ),
    );
  }

  Widget _buildViewChip(CalendarView view, String name, IconData icon) {
    return ChoiceChip(
      label: _calendarController.view == view
          ? Text(name, style: const TextStyle(color: Colors.white))
          : Icon(icon), // Show text only when selected
      selected: _calendarController.view == view,
      onSelected: (bool selected) {
        if (selected) {
          setState(() {
            _calendarController.view = view;
          });
        }
      },
      // Optional styling
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(8.0), // Rounded corners
      ),
      padding: const EdgeInsets.symmetric(
          horizontal: 12.0, vertical: 8.0), // Adjust padding
      labelPadding: const EdgeInsets.all(2.0),
    );
  }

  DateTime _getSelectedDate() {
    final selectedDate = _calendarController.selectedDate;
    return selectedDate ?? DateTime.now();
  }

  Future<void> _showEventDetailsDialog(
      BuildContext context, EventDTO event) async {
    showDialog<void>(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text(event.title),
          content: SingleChildScrollView(
            child: ListBody(
              children: <Widget>[
                Text('Description: ${event.description}'),
                if (!event.allDay!) // Conditionally show start/end times
                  Text('Start: ${DateFormat.yMd().add_jm().format(event.start)}'),
                if (!event.allDay!)
                  Text('End: ${DateFormat.yMd().add_jm().format(event.end)}'),
                if (event.allDay!) Text('All Day Event'), // Show "All Day"
                Text('Location: ${event.location?? "Not specified"}'), // Provide a default,
                //... other event details
              ],
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text('Close'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  Future<void> _showAddEventDialog(BuildContext context) async {
    final selectedDate = _getSelectedDate();
    bool allDay = false;
    bool isRecurrent = false;
    String? selectedColor = 'blue';
    String? selectedRecurrenceRule;
    DateTime startDateTime = DateTime(selectedDate.year, selectedDate.month, selectedDate.day, 15, 0);
    DateTime endDateTime = startDateTime.add(Duration(hours: 1));


    return showDialog<void>(
      context: context,
      builder: (BuildContext context) {
        final screenSize = MediaQuery.of(context).size; // Bildschirmgröße
        final popupWidth = screenSize.width * 0.9; // 90% der Bildschirmbreite

        return AlertDialog(
          title: const Text('Add Event'),
          content: SizedBox(
            width: popupWidth,
            child: SingleChildScrollView(
              child: StatefulBuilder(
                builder: (context, setState) {
                  return Column(
                    mainAxisSize: MainAxisSize.min,
                    children: <Widget>[
                      TextField(
                        controller: _titleController,
                        decoration: const InputDecoration(hintText: 'Title'),
                      ),
                      TextField(
                        controller: _descriptionController,
                        decoration:
                            const InputDecoration(hintText: 'Description'),
                        maxLines: 3,
                      ),

                      // Checkbox für ganztägige Termine
                      Row(
                        children: [
                          Checkbox(
                            value: allDay,
                            onChanged: (bool? value) {
                              setState(() {
                                allDay = value!;
                              });
                            },
                          ),
                          const Text('All-day'),
                        ],
                      ),

                      // Startdatum und -uhrzeit
                      if (!allDay) // Nur anzeigen, wenn nicht ganztägig
                        Row(
                          children: [
                            Text(
                              "Start:\n${DateFormat.yMMMEd().add_jm().format(startDateTime)}",
                            ),
                            IconButton(
                              onPressed: () async {
                                final DateTime? pickedDate =
                                    await showDatePicker(
                                  context: context,
                                  initialDate: startDateTime,
                                  firstDate: DateTime(2000),
                                  lastDate: DateTime(2101),
                                  
                                );
                                if (pickedDate != null &&
                                    pickedDate != startDateTime) {
                                  setState(() {
                                    startDateTime = DateTime(
                                      pickedDate.year,
                                      pickedDate.month,
                                      pickedDate.day,
                                      startDateTime.hour,
                                      startDateTime.minute,
                                    );
                                  });
                                }
                              },
                              icon: const Icon(Icons.calendar_today),
                            ),
                            IconButton(
                              onPressed: () async {
                                final TimeOfDay? pickedTime =
                                    await showTimePicker(
                                  context: context,
                                  initialTime:
                                      TimeOfDay.fromDateTime(startDateTime),
                                );
                                if (pickedTime != null) {
                                  setState(() {
                                    startDateTime = DateTime(
                                      startDateTime.year,
                                      startDateTime.month,
                                      startDateTime.day,
                                      pickedTime.hour,
                                      pickedTime.minute,
                                    );
                                  });
                                }
                              },
                              icon: const Icon(Icons.access_time),
                            ),
                          ],
                        ),
                      if (allDay)
                        Row(
                          children: [
                            Text(
                              'Start:\n${DateFormat.yMMMEd().format(startDateTime)}',
                            ),
                            IconButton(
                              onPressed: () async {
                                final DateTime? pickedDate =
                                    await showDatePicker(
                                  context: context,
                                  initialDate: startDateTime,
                                  firstDate: DateTime(2000),
                                  lastDate: DateTime(2101),
                                );
                                if (pickedDate != null &&
                                    pickedDate != startDateTime) {
                                  setState(() {
                                    startDateTime = pickedDate;
                                  });
                                }
                              },
                              icon: const Icon(Icons.calendar_today),
                            ),
                          ],
                        ),

                      // Enddatum und -uhrzeit (ähnlich wie Start)
                      if (!allDay) // Nur anzeigen, wenn nicht ganztägig
                        Row(
                          children: [
                            Text(
                              'Ende:\n${DateFormat.yMMMEd().add_jm().format(endDateTime)}',
                            ),
                            IconButton(
                              onPressed: () async {
                                final DateTime? pickedDate =
                                    await showDatePicker(
                                  context: context,
                                  initialDate: endDateTime,
                                  firstDate: DateTime(2000),
                                  lastDate: DateTime(2101),
                                );
                                if (pickedDate != null &&
                                    pickedDate != endDateTime) {
                                  setState(() {
                                    endDateTime = DateTime(
                                      pickedDate.year,
                                      pickedDate.month,
                                      pickedDate.day,
                                      endDateTime.hour,
                                      endDateTime.minute,
                                    );
                                  });
                                }
                              },
                              icon: const Icon(Icons.calendar_today),
                            ),
                            IconButton(
                              onPressed: () async {
                                final TimeOfDay? pickedTime =
                                    await showTimePicker(
                                  context: context,
                                  initialTime:
                                      TimeOfDay.fromDateTime(endDateTime),
                                );
                                if (pickedTime != null) {
                                  setState(() {
                                    endDateTime = DateTime(
                                      endDateTime.year,
                                      endDateTime.month,
                                      endDateTime.day,
                                      pickedTime.hour,
                                      pickedTime.minute,
                                    );
                                  });
                                }
                              },
                              icon: const Icon(Icons.access_time),
                            ),
                          ],
                        ),
                      if (allDay)
                        Row(
                          children: [
                            Text(
                              'Ende:\n${DateFormat.yMMMEd().format(endDateTime)}',
                            ),
                            IconButton(
                              onPressed: () async {
                                final DateTime? pickedDate =
                                    await showDatePicker(
                                  context: context,
                                  initialDate: endDateTime,
                                  firstDate: DateTime(2000),
                                  lastDate: DateTime(2101),
                                );
                                if (pickedDate != null &&
                                    pickedDate != endDateTime) {
                                  setState(() {
                                    endDateTime = pickedDate;
                                  });
                                }
                              },
                              icon: const Icon(Icons.calendar_today),
                            ),
                          ],
                        ),

                      // Farbauswahl
                      DropdownButtonFormField<String>(
                        decoration: const InputDecoration(labelText: 'Color'),
                        value: selectedColor ?? 'blue',
                        items: <String>[
                          'red',
                          'blue',
                          'green',
                          'yellow',
                          'purple',
                        ].map((String value) {
                          return DropdownMenuItem<String>(
                            value: value,
                            child: Text(value),
                          );
                        }).toList(),
                        onChanged: (String? newValue) {
                          setState(() {
                            selectedColor = newValue;
                          });
                        },
                      ),

                      // Wiederholung (Checkbox)
                      Row(
                        children: [
                          Checkbox(
                            value: isRecurrent,
                            onChanged: (bool? value) {
                              setState(() {
                                isRecurrent = value!;
                              });
                            },
                          ),
                          const Text('Recurrent'),
                        ],
                      ),

                      // Dropdown für Wiederholungsregel (nur anzeigen, wenn wiederkehrend)
                      if (isRecurrent)
                        DropdownButtonFormField<String>(
                          decoration: const InputDecoration(
                              labelText: 'Recurrence Rule'),
                          value: selectedRecurrenceRule,
                          items: _recurrenceRules.map((String value) {
                            return DropdownMenuItem<String>(
                              value: value,
                              child: Text(value),
                            );
                          }).toList(),
                          onChanged: (String? newValue) {
                            setState(() {
                              _selectedRecurrenceRule = newValue;
                            });
                          },
                        ),
                    ],
                  );
                },
              ),
            ),
          ),
          actions: <Widget>[
            TextButton(
              child: const Text('Cancel'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: const Text('Add'),
              onPressed: () {
                _allDay =
                    allDay; // Übertrage die Werte an den äußeren Scope
                _isRecurrent =
                    isRecurrent; // Übertrage die Werte an den äußeren Scope
                _selectedColor =
                    selectedColor; // Übertrage die Werte an den äußeren Scope
                _selectedRecurrenceRule =
                    selectedRecurrenceRule; // Übertrage die Werte an den äußeren Scope
                _startDateTime =
                    startDateTime; // Übertrage die Werte an den äußeren Scope
                _endDateTime =
                    endDateTime; // Übertrage die Werte an den äußeren Scope
                    //Damit der Termin über den ganzen Tag geht
                if (allDay) {
                  _startDateTime = DateTime(_startDateTime.year, _startDateTime.month, _startDateTime.day, 0, 0, 0); // Midnight
                  _endDateTime = DateTime(_endDateTime.year, _endDateTime.month, _endDateTime.day + 1, 0, 0, 0); // Midnight of the next day
                }

                _addEvent();

                
                Navigator.of(context).maybePop();
              },
            ),
          ],
        );
      },
    );
  }
}
