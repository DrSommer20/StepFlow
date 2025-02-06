package de.sommer.stepflowBackend.dto;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import de.sommer.stepflowBackend.models.Event;
import de.sommer.stepflowBackend.models.User;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
"id",
"title",
"description",
"start",
"end",
"location",
"attendees",
"color",
"recurrent",
"recurrenceRule"
})
public class EventDTO {

@JsonProperty("id")
private int id;
@JsonProperty("title")
private String title;
@JsonProperty("description")
private String description;
@JsonProperty("start")
private String start;
@JsonProperty("end")
private String end;
@JsonProperty("location")
private String location;
@JsonProperty("attendees")
private List<String> attendees;
@JsonProperty("color")
private String color;
@JsonProperty("recurrent")
private String recurrent;
@JsonProperty("recurrenceRule")
private String recurrenceRule;
@JsonProperty("allDay")
private boolean allDay;
@JsonIgnore
private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

/**
* No args constructor for use in serialization
*
*/
public EventDTO() {
}

public EventDTO(int id, String title, String description, String start, String end, String location, List<String> attendees, String color, String recurrent, String recurrenceRule, boolean allDay) {
super();
this.id = id;
this.title = title;
this.description = description;
this.start = start;
this.end = end;
this.location = location;
this.attendees = attendees;
this.color = color;
this.recurrent = recurrent;
this.recurrenceRule = recurrenceRule;
this.allDay = allDay;
}

public EventDTO(Event event) {
super();
this.id = event.getId();
this.title = event.getTitle();
this.description = event.getDescription();
this.start = event.getStart().toString();
this.end = event.getEnd().toString();
this.location = event.getLocation();
this.attendees = new ArrayList<String>();
for(User user : event.getAttendees()) {
    this.attendees.add(String.valueOf(user.getId()));
}

this.color = event.getColor();
this.recurrent = Boolean.toString(event.isRecurrent());
this.recurrenceRule = event.getRecurrenceRule();

}

@JsonProperty("id")
public int getId() {
return id;
}

@JsonProperty("id")
public void setId(int id) {
this.id = id;
}

@JsonProperty("title")
public String getTitle() {
return title;
}

@JsonProperty("title")
public void setTitle(String title) {
this.title = title;
}

@JsonProperty("description")
public String getDescription() {
return description;
}

@JsonProperty("description")
public void setDescription(String description) {
this.description = description;
}

@JsonProperty("start")
public String getStart() {
return start;
}

@JsonProperty("start")
public void setStart(String start) {
this.start = start;
}

@JsonProperty("end")
public String getEnd() {
return end;
}

@JsonProperty("end")
public void setEnd(String end) {
this.end = end;
}

@JsonProperty("location")
public String getLocation() {
return location;
}

@JsonProperty("location")
public void setLocation(String location) {
this.location = location;
}

@JsonProperty("attendees")
public List<String> getAttendees() {
return attendees;
}

@JsonProperty("attendees")
public void setAttendees(List<String> attendees) {
this.attendees = attendees;
}

@JsonProperty("color")
public String getColor() {
return color;
}

@JsonProperty("color")
public void setColor(String color) {
this.color = color;
}

@JsonProperty("recurrent")
public String getRecurrent() {
return recurrent;
}

@JsonProperty("recurrent")
public void setRecurrent(String recurrent) {
this.recurrent = recurrent;
}

@JsonProperty("recurrenceRule")
public String getRecurrenceRule() {
return recurrenceRule;
}

@JsonProperty("recurrenceRule")
public void setRecurrenceRule(String recurrenceRule) {
this.recurrenceRule = recurrenceRule;
}

@JsonProperty("allDay")
public boolean isAllDay() {
return allDay;
}

@JsonProperty("allDay")
public void setAllDay(boolean allDay) {
this.allDay = allDay;
}



@JsonAnyGetter
public Map<String, Object> getAdditionalProperties() {
return this.additionalProperties;
}

@JsonAnySetter
public void setAdditionalProperty(String name, Object value) {
this.additionalProperties.put(name, value);
}

}
