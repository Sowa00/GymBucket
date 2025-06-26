import { useState } from 'react'
import { Apple, Bell, Calendar, Dumbbell, Plus, Search, Users } from 'lucide-react'

import './styles.scss'

interface IClient {
  id: number
  name: string
  time: string
  type: string
}

const Homepage = () => {
  const [selectedTab, setSelectedTab] = useState('overview')

  // Mock data
  const upcomingClients: IClient[] = [
    { id: 1, name: 'Jakub Sowa', time: '9:00 AM', type: 'Klata' },
    { id: 2, name: 'Aleksander Skrzypiec', time: '11:30 AM', type: 'Cardio' },
  ]

  const recentClients = [
    { id: 1, name: 'Aleksander Skrzypiec', lastSession: '2 days ago', progress: '+5 kg' },
    { id: 2, name: 'Jakub Sowa', lastSession: '1 day ago', progress: '-2% body fat' },
  ]

  const stats = {
    totalClients: { value: '24', label: 'Total Clients', icon: Users },
    todaysSessions: { value: '6', label: "Today's Sessions", icon: Calendar },
    weeklyRevenue: { value: '$2,480', label: 'Weekly Revenue', icon: Dumbbell },
    completionRate: { value: '94%', label: 'Completion Rate', icon: Apple },
  }

  const getInitials = (name: string) => {
    return name
      .split(' ')
      .map((n) => n[0])
      .join('')
  }

  return (
    <div className='dashboard'>
      {/* Main Content */}
      <div className='main-content'>
        {/* Header */}
        <div className='header'>
          <div className='header__title'>
            <h2>Welcome back, Coach!</h2>
            <p>Here's what's happening with your clients today</p>
          </div>

          <div className='header__actions'>
            <div className='header__search'>
              <Search />
              <input
                type='text'
                placeholder='Search clients...'
              />
            </div>
            <button className='header__notification'>
              <Bell />
            </button>
            <button className='header__cta'>
              <Plus />
              <span>New Client</span>
            </button>
          </div>
        </div>

        {/* Stats Cards */}
        <div className='stats'>
          {Object.entries(stats).map(([key, { value, label, icon: Icon }]) => (
            <div
              key={key}
              className='stats__card'
            >
              <div className='stats__card-header'>
                <div className='stats__card-icon'>
                  <Icon />
                </div>
              </div>
              <h3 className='stats__card-value'>{value}</h3>
              <p className='stats__card-label'>{label}</p>
            </div>
          ))}
        </div>

        {/* Main Content Grid */}
        <div className='content-grid'>
          {/* Today's Schedule */}
          <div className='schedule'>
            <div className='schedule__header'>
              <h3>Today's Schedule</h3>
              <button>View All</button>
            </div>

            {upcomingClients.map((client) => (
              <div
                key={client.id}
                className='schedule__item'
              >
                <div className='schedule__item-left'>
                  <div className='schedule__item-avatar'>{getInitials(client.name)}</div>
                  <div className='schedule__item-info'>
                    <h4>{client.name}</h4>
                    <p>{client.type}</p>
                  </div>
                </div>
                <div className='schedule__item-right'>
                  <p>{client.time}</p>
                  <button>View Details</button>
                </div>
              </div>
            ))}

            <button className='schedule__add'>+ Add New Session</button>
          </div>

          {/* Recent Activity */}
          <div className='activity'>
            <h3 className='activity__header'>Recent Client Activity</h3>

            {recentClients.map((client) => (
              <div
                key={client.id}
                className='activity__item'
              >
                <div className='activity__item-header'>
                  <div className='activity__item-avatar'>{getInitials(client.name)}</div>
                  <div className='activity__item-info'>
                    <h4>{client.name}</h4>
                    <p>{client.lastSession}</p>
                  </div>
                </div>
                <p className='activity__item-progress'>{client.progress}</p>
              </div>
            ))}

            <button className='activity__cta'>View All Clients</button>
          </div>
        </div>

        {/* Quick Actions */}
        <div className='quick-actions'>
          <button className='quick-actions__card quick-actions__card--workout'>
            <Dumbbell />
            <h3>Create Workout Plan</h3>
            <p>Design custom workout routines</p>
          </button>

          <button className='quick-actions__card quick-actions__card--nutrition'>
            <Apple />
            <h3>Nutrition Plan</h3>
            <p>Build meal plans and track nutrition</p>
          </button>

          <button className='quick-actions__card quick-actions__card--schedule'>
            <Calendar />
            <h3>Schedule Session</h3>
            <p>Book appointments with clients</p>
          </button>
        </div>
      </div>
    </div>
  )
}

export default Homepage
