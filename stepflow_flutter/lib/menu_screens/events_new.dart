import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:easy_date_timeline/easy_date_timeline.dart';
import 'package:intl/intl.dart';
import 'package:ionicons/ionicons.dart';
import 'package:shared_preferences/shared_preferences.dart';
import 'package:stepflow/login_screen.dart';
import 'package:stepflow/menu_screens/event_dto.dart';
import 'package:scrollable_positioned_list/scrollable_positioned_list.dart';

import 'package:http/http.dart' as http;

class EventsNewScreen extends StatefulWidget {
  const EventsNewScreen({super.key});
  

  @override
  State<EventsNewScreen> createState() => _EventsNewScreenState();
}

class _EventsNewScreenState extends State<EventsNewScreen> {

  final String apiUrl = 'http://192.168.0.136:8080/api/events';
  DateTime _selectedDate = DateTime.now();
  List<EventDTO> events = [];
  String? _authToken; // Store the auth token
  bool _eventsLoaded = false;

  final TextEditingController _titleController = TextEditingController();
  final TextEditingController _descriptionController = TextEditingController();
  bool _allDay = false;
  DateTime _startDateTime = DateTime.now();
  DateTime _endDateTime = DateTime.now().add(const Duration(hours: 1));
  String? _selectedColor;
  bool _isRecurrent = false;
  String? _selectedRecurrenceRule;
  final startDate = DateTime.now().subtract(const Duration(days: 365 ~/ 2));
  final endDate = DateTime.now().add(const Duration(days: 365 * 3 ~/ 2));

  final ItemScrollController _scrollController = ItemScrollController();

  final List<String> _recurrenceRules = [
    'DAILY',
    'WEEKLY',
    'MONTHLY',
    'YEARLY',
  ];

  @override
  void initState() {
    super.initState();
    _loadTokenAndEvents(); // Load events on screen initialization
  }

  Future<void> _loadTokenAndEvents() async {
    final prefs = await SharedPreferences.getInstance();
    _authToken = prefs.getString('token');
    if (_authToken != null) {
      await _loadEvents();
      _eventsLoaded = true; // Set flag after loading
      _scrollToToday(); // Scroll after events are loaded
    } else {
      if (mounted) {
        Navigator.push(
          // Use Navigator.push for navigation
          context,
          MaterialPageRoute(builder: (context) => LoginScreen(onLoginSuccess: _loadTokenAndEvents)),
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
        setState(() {
        _eventsLoaded = true; // Set the flag to true here as well
      });
      _scrollToToday(); // Scroll after events are loaded.
      } else {
        if (mounted) {
          ScaffoldMessenger.of(context).showSnackBar(
            const SnackBar(content: Text('Error loading events. Check console.')),
          );
        }
      }
    } else {
      if (mounted) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
              content: Text('Failed to load events: ${response.statusCode}')),
        );
      }
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
      location: 'Home',
    );

    final response = await http.post(
      Uri.parse(apiUrl),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $_authToken',
        'Team': '1',
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
      if(mounted){
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text('Failed to add event: ${response.statusCode}')),
        );
      }
      
    }
  }


  void _scrollToSelectedDate() {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final index = _getDateIndex(_selectedDate);
      if (index != -1) {
        _scrollController.scrollTo(
          index: index,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeInOut,
        );
      }
    });
  }

  void _scrollToToday() {
    if (!_eventsLoaded) return; // Don't scroll if events are not loaded yet
    WidgetsBinding.instance.addPostFrameCallback((_) {
      final today = DateTime.now();
      final index = _getDateIndex(today);
      if (index != -1) {
        _scrollController.scrollTo(
          index: index,
          duration: const Duration(milliseconds: 300),
          curve: Curves.easeInOut,
        );
      }
    });
  }

  int _getDateIndex(DateTime date) {
    final dateRange = _getDateRange(startDate, endDate);
    return dateRange.indexOf(date);
  }

  @override
  Widget build(BuildContext context) {
    WidgetsBinding.instance.addPostFrameCallback((_) {
      _scrollToToday();
    });

    return Scaffold(
      appBar: PreferredSize(
      preferredSize: Size.fromHeight(75.0),
      child: AppBar(
        automaticallyImplyLeading: false, // Remove back button
        title: Padding(
        padding: const EdgeInsets.only(top: 40.0),
        child: const Text(
          'Termin Übersicht',
          style: TextStyle(fontSize: 24.0, fontWeight: FontWeight.bold), // Larger title
        ),
        ),
        centerTitle: true,  // Center the title if you want
        elevation: 5, 
      ),
      ),
      body: Column(
      children: [
        // Categories selection
        Padding( // Add padding for better spacing
        padding: const EdgeInsets.all(16.0),
        child: Row(
          children: [
          Column(
            crossAxisAlignment: CrossAxisAlignment.start, // Bind to the left side
            children: [
            const Text('Alle Termine', style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16.0)),
            const Text('1 Training heute', style: TextStyle(fontWeight: FontWeight.w200, fontSize: 14.0)),
            ],
          ),
          const Spacer(), // Add a spacer to push the button to the right
          ElevatedButton(
            style: ElevatedButton.styleFrom(
            backgroundColor: Colors.transparent, // Transparent background
            side: BorderSide(color: Colors.lightBlue, width: 2.0), // Light blue outline
            shape: CircleBorder(), // Circular shape
            ),
            onPressed: () {
            _showAddEventDialog(context); // Show the add event dialog
            },
            child: const Icon(Ionicons.calendar_number), // Add icon to button
          ),
          ],
        ),
        ),
        // Event list for the selected date
        EasyDateTimeLinePicker(
        selectionMode: SelectionMode.autoCenter(),
        locale: Locale('de', 'DE'), // Set the locale to German
        firstDate: startDate,
        lastDate: endDate,
        focusedDate: _selectedDate,
        onDateChange: (date) {
          setState(() {
          _selectedDate = date;
          _scrollToSelectedDate();
          });
        },
        ),
        // Add an Expanded widget to push the date picker to the top
        // and allow for more content below it.
        Expanded(
        child: _buildEventList(), // You can add your event list or other content here.
        ),
      ],
      ),
    );
    
  }

  Widget _buildEventList() {
    // 1. Get date range (2 years before and after today)
    final dateRange = _getDateRange(startDate, endDate);

    // 2. Group events by date
    final eventsByDate = <DateTime, List<EventDTO>>{};
    for (final event in events) {
      final date = DateTime(event.start.year, event.start.month, event.start.day);
      eventsByDate.putIfAbsent(date, () => []);
      eventsByDate[date]!.add(event);
    }

    // 3. Build the list
    return ScrollablePositionedList.builder(
      itemScrollController: _scrollController,
      itemCount: dateRange.length,
      itemBuilder: (context, index) {
        final date = dateRange[index];
        final eventsForDate = eventsByDate[date] ?? []; // Show empty list if no events

        return Column(
          key: index == _getDateIndex(_selectedDate) ? GlobalKey() : null, // Assign key to selected date item
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.all(16.0),
              child: Text(
                DateFormat.yMMMEd('de').format(date),
                style: const TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
              ),
            ),
            // Display event boxes (or an empty message)
            if (eventsForDate.isEmpty)
              _buildNoEventsMessage()
            else
              for (final event in eventsForDate) _buildEventBox(event),
          ],
        );
      },
    );
  }

  List<DateTime> _getDateRange(DateTime start, DateTime end) {
    List<DateTime> range = [];
    for (var date = start; date.isBefore(end); date = date.add(const Duration(days: 1))) {
      range.add(DateTime(date.year, date.month, date.day)); // Add date without time
    }
    return range;
  }

  Widget _buildNoEventsMessage() {
    return const Padding(
      padding: EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Text(
        "Keine Termine für diesen Tag", // German for "No events for this day"
        style: TextStyle(color: Colors.grey),
      ),
    );
  }

  Widget _buildEventBox(EventDTO event) {
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Container(
        width: double.infinity, // Use full screen width
        decoration: BoxDecoration(
          color: Colors.blue, // ... (Get color from event.color)
          borderRadius: BorderRadius.circular(8.0),
        ),
        padding: const EdgeInsets.all(16.0),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween, // Align title and time
              children: [
                Expanded( // Expand title to take available space
                  child: Text(
                    event.title,
                    style: const TextStyle(
                      color: Colors.white,
                      fontSize: 16,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                Text(
                  event.allDay!
                      ? 'Ganztägig' // German for All Day
                      : '${DateFormat.jm('de').format(event.start)} - ${DateFormat.jm('de').format(event.end)}', // German time format
                  style: const TextStyle(color: Colors.white),
                ),
              ],
            ),
            const SizedBox(height: 8),
            // ... (Add other event details as needed)
          ],
        ),
      ),
    );
  }
  Future<void> _showAddEventDialog(BuildContext context) async {
    final selectedDate = _selectedDate;
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