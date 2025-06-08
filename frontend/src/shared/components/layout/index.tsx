import { Outlet, useNavigate } from 'react-router-dom'
import { Apple, Calendar, Dumbbell, Home, LogOut, Settings, Users } from 'lucide-react'
import { useState } from 'react'
// chuj kurwo do wylogowywania
// chuj kurwo do wylogowywania token JVT zwraca backend do logowania nie
//jak zwroci jvt to w local storage go zapisujemy
//w apptsx sprawdzamy local

interface INavItem {
  id: string
  label: string
  icon: any
}

const navItems: INavItem[] = [
  { id: 'overview', label: 'Dashboard', icon: Home },
  { id: 'clients', label: 'Clients', icon: Users },
  { id: 'calendar', label: 'Calendar', icon: Calendar },
  { id: 'workouts', label: 'Workout Plans', icon: Dumbbell },
  { id: 'nutrition', label: 'Nutrition Plans', icon: Apple },
  { id: 'settings', label: 'Settings', icon: Settings },
]

const Layout = () => {
  const [selectedTab, setSelectedTab] = useState('overview')
  const navigate = useNavigate()

  const handleNavigate = (id: string) => {
    setSelectedTab(id)
    navigate(`/${id}`)
  }

  return (
    <>
      <div className='sidebar'>
        <div className='sidebar__header'>
          <div className='sidebar__header-logo'>
            <Dumbbell />
          </div>
          <h1 className='sidebar__header-title'>GymBucket</h1>
        </div>

        <nav className='sidebar__nav'>
          {navItems.map(({ id, label, icon: Icon }) => (
            <button
              key={id}
              onClick={() => handleNavigate(id)}
              className={`sidebar__nav-item ${selectedTab === id ? 'sidebar__nav-item--active' : ''}`}
            >
              <Icon />
              <span>{label}</span>
            </button>
          ))}
        </nav>

        <button className='sidebar__logout'>
          <LogOut />
          <span>Sign Out</span>
        </button>
      </div>
      <Outlet />
    </>
  )
}

export default Layout
