class EventDTO {
  int? id;
  String title;
  String description;
  DateTime start;
  DateTime end;
  String? location;
  List<EventAttendeeDTO>? attendees;
  String? color;
  bool? recurrent;
  String? recurrenceRule;
  bool? allDay;
  int? teamId;

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
    this.teamId,
  });

  factory EventDTO.fromJson(Map<String, dynamic> json) {
    print(json);
    return EventDTO(
      id: json['id'],
      title: json['title'],
      description: json['description'] ?? '',
      start: DateTime.parse(json['start']),
      end: DateTime.parse(json['end']),
      location: json['location'],
      attendees: json['attendees'] != null
          ? (json['attendees'] as List)
              .map((attendee) => EventAttendeeDTO.fromJson(attendee))
              .toList()
          : null,
      color: json['color'],
      recurrent: json['recurrent'] != null
          ? json['recurrent'].toString().toLowerCase() == 'true'
          : null,
      recurrenceRule: json['recurrenceRule'],
      allDay: json['allDay'],
      teamId: json['teamId'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'title': title,
      'description': description,
      'start': start.toIso8601String(),
      'end': end.toIso8601String(),
      'location': location,
      'attendees': attendees?.map((attendee) => attendee.toJson()).toList(),
      'color': color,
      'recurrent': recurrent?.toString(),
      'recurrenceRule': recurrenceRule,
      'allDay': allDay,
      'teamId': teamId,
    };
  }
}

class EventAttendeeDTO {
  int userId;
  String status;

  EventAttendeeDTO({
    required this.userId,
    required this.status,
  });

  factory EventAttendeeDTO.fromJson(Map<String, dynamic> json) {
    return EventAttendeeDTO(
      userId: json['userId'],
      status: json['status'],
    );
  }

  Map<String, dynamic> toJson() {
    return {
      'userId': userId,
      'status': status,
    };
  }
}