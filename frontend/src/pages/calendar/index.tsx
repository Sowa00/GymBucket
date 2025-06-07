import { useState } from 'react'
import { ChevronLeft, ChevronRight, Clock, Edit, MapPin, Plus, Trash2, User } from 'lucide-react'
import './styles.scss'

interface CalendarEvent {
  id: string
  title: string
  client: string
  time: string
  duration: number
  type: 'workout' | 'nutrition' | 'consultation'
  location?: string
  date: Date
}

interface CalendarProps {
  events?: CalendarEvent[]
}

const Calendar = ({ events = [] }: CalendarProps) => {
  const [currentDate, setCurrentDate] = useState(new Date())
  const [selectedDate, setSelectedDate] = useState<Date | null>(null)
  const [viewMode, setViewMode] = useState<'month' | 'week' | 'day'>('month')
  const [showEventModal, setShowEventModal] = useState(false)

  // Mock events data
  const mockEvents: CalendarEvent[] = [
    {
      id: '1',
      title: 'Strength Training Session',
      client: 'Sarah Johnson',
      time: '09:00',
      duration: 60,
      type: 'workout',
      location: 'Gym Floor A',
      date: new Date(2025, 5, 8),
    },
    {
      id: '2',
      title: 'Nutrition Consultation',
      client: 'Mike Chen',
      time: '11:30',
      duration: 45,
      type: 'nutrition',
      location: 'Office',
      date: new Date(2025, 5, 8),
    },
    {
      id: '3',
      title: 'HIIT Workout',
      client: 'Emma Davis',
      time: '14:00',
      duration: 45,
      type: 'workout',
      location: 'Gym Floor B',
      date: new Date(2025, 5, 9),
    },
    {
      id: '4',
      title: 'Progress Review',
      client: 'Alex Rodriguez',
      time: '16:00',
      duration: 30,
      type: 'consultation',
      location: 'Office',
      date: new Date(2025, 5, 10),
    },
    {
      id: '5',
      title: 'Cardio Session',
      client: 'Lisa Park',
      time: '08:00',
      duration: 60,
      type: 'workout',
      location: 'Cardio Zone',
      date: new Date(2025, 5, 12),
    },
  ]

  const allEvents = [...events, ...mockEvents]

  const monthNames = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December',
  ]

  const dayNames = ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']

  const getDaysInMonth = (date: Date) => {
    const year = date.getFullYear()
    const month = date.getMonth()
    const firstDay = new Date(year, month, 1)
    const lastDay = new Date(year, month + 1, 0)
    const daysInMonth = lastDay.getDate()
    const startingDayOfWeek = firstDay.getDay()

    const days = []

    // Add empty cells for days before the first day of the month
    for (let i = 0; i < startingDayOfWeek; i++) {
      days.push(null)
    }

    // Add all days of the month
    for (let day = 1; day <= daysInMonth; day++) {
      days.push(new Date(year, month, day))
    }

    return days
  }

  const getEventsForDate = (date: Date) => {
    return allEvents.filter((event) => event.date.toDateString() === date.toDateString())
  }

  const navigateMonth = (direction: 'prev' | 'next') => {
    setCurrentDate((prev) => {
      const newDate = new Date(prev)
      if (direction === 'prev') {
        newDate.setMonth(prev.getMonth() - 1)
      } else {
        newDate.setMonth(prev.getMonth() + 1)
      }
      return newDate
    })
  }

  const isToday = (date: Date) => {
    const today = new Date()
    return date.toDateString() === today.toDateString()
  }

  const isSelected = (date: Date) => {
    return selectedDate && date.toDateString() === selectedDate.toDateString()
  }

  const getEventTypeClass = (type: CalendarEvent['type']) => {
    switch (type) {
      case 'workout':
        return 'calendar__event--workout'
      case 'nutrition':
        return 'calendar__event--nutrition'
      case 'consultation':
        return 'calendar__event--consultation'
      default:
        return ''
    }
  }

  const formatTime = (time: string, duration: number) => {
    const [hours, minutes] = time.split(':').map(Number)
    const endHours = Math.floor((hours * 60 + minutes + duration) / 60)
    const endMinutes = (hours * 60 + minutes + duration) % 60

    return `${time} - ${endHours.toString().padStart(2, '0')}:${endMinutes.toString().padStart(2, '0')}`
  }

  const renderMonthView = () => {
    const days = getDaysInMonth(currentDate)

    return (
      <div className='calendar__month'>
        <div className='calendar__weekdays'>
          {dayNames.map((day) => (
            <div
              key={day}
              className='calendar__weekday'
            >
              {day}
            </div>
          ))}
        </div>

        <div className='calendar__days'>
          {days.map((day, index) => (
            <div
              key={index}
              className={`calendar__day ${!day ? 'calendar__day--empty' : ''} ${
                day && isToday(day) ? 'calendar__day--today' : ''
              } ${day && isSelected(day) ? 'calendar__day--selected' : ''}`}
              onClick={() => day && setSelectedDate(day)}
            >
              {day && (
                <>
                  <span className='calendar__day-number'>{day.getDate()}</span>
                  <div className='calendar__day-events'>
                    {getEventsForDate(day)
                      .slice(0, 3)
                      .map((event) => (
                        <div
                          key={event.id}
                          className={`calendar__event ${getEventTypeClass(event.type)}`}
                          title={`${event.title} - ${event.client}`}
                        >
                          {event.time} {event.title}
                        </div>
                      ))}
                    {getEventsForDate(day).length > 3 && (
                      <div className='calendar__event-more'>
                        +{getEventsForDate(day).length - 3} more
                      </div>
                    )}
                  </div>
                </>
              )}
            </div>
          ))}
        </div>
      </div>
    )
  }

  const renderSidebar = () => {
    const todayEvents = getEventsForDate(selectedDate || new Date())

    return (
      <div className='calendar__sidebar'>
        <div className='calendar__sidebar-header'>
          <h3>
            {selectedDate
              ? selectedDate.toLocaleDateString('en-US', {
                  weekday: 'long',
                  month: 'long',
                  day: 'numeric',
                })
              : "Today's Schedule"}
          </h3>
          <button
            className='calendar__add-event'
            onClick={() => setShowEventModal(true)}
          >
            <Plus size={16} />
            Add Event
          </button>
        </div>

        <div className='calendar__events-list'>
          {todayEvents.length === 0 ? (
            <div className='calendar__no-events'>
              <p>No events scheduled</p>
            </div>
          ) : (
            todayEvents.map((event) => (
              <div
                key={event.id}
                className={`calendar__event-card ${getEventTypeClass(event.type)}`}
              >
                <div className='calendar__event-header'>
                  <h4>{event.title}</h4>
                  <div className='calendar__event-actions'>
                    <button className='calendar__event-action'>
                      <Edit size={14} />
                    </button>
                    <button className='calendar__event-action'>
                      <Trash2 size={14} />
                    </button>
                  </div>
                </div>

                <div className='calendar__event-details'>
                  <div className='calendar__event-detail'>
                    <User size={14} />
                    <span>{event.client}</span>
                  </div>
                  <div className='calendar__event-detail'>
                    <Clock size={14} />
                    <span>{formatTime(event.time, event.duration)}</span>
                  </div>
                  {event.location && (
                    <div className='calendar__event-detail'>
                      <MapPin size={14} />
                      <span>{event.location}</span>
                    </div>
                  )}
                </div>
              </div>
            ))
          )}
        </div>
      </div>
    )
  }

  return (
    <div className='calendar'>
      <div className='calendar__header'>
        <div className='calendar__navigation'>
          <button
            className='calendar__nav-btn'
            onClick={() => navigateMonth('prev')}
          >
            <ChevronLeft size={20} />
          </button>

          <h2 className='calendar__title'>
            {monthNames[currentDate.getMonth()]} {currentDate.getFullYear()}
          </h2>

          <button
            className='calendar__nav-btn'
            onClick={() => navigateMonth('next')}
          >
            <ChevronRight size={20} />
          </button>
        </div>

        <div className='calendar__view-controls'>
          {(['month', 'week', 'day'] as const).map((mode) => (
            <button
              key={mode}
              className={`calendar__view-btn ${viewMode === mode ? 'calendar__view-btn--active' : ''}`}
              onClick={() => setViewMode(mode)}
            >
              {mode.charAt(0).toUpperCase() + mode.slice(1)}
            </button>
          ))}
        </div>
      </div>

      <div className='calendar__content'>
        <div className='calendar__main'>{renderMonthView()}</div>
        {renderSidebar()}
      </div>
    </div>
  )
}

export default Calendar
