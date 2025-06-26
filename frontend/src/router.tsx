import { createBrowserRouter, RouteObject } from 'react-router-dom'
import LoginPage from './pages/login'
import Layout from './shared/components/layout'
import Homepage from './pages/homepage'
import Calendar from './pages/calendar'
import App from './App.tsx'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <LoginPage />,
  },
  {
    path: '/login',
    element: <LoginPage />,
  },
  {
    path: '/homepage',
    element: (
      <App>
        <Layout />
      </App>
    ),
    children: [
      {
        index: true,
        element: <Homepage />,
      },
    ],
  },
  {
    path: '/calendar',
    element: (
      <App>
        <Layout />
      </App>
    ),
    children: [
      {
        index: true,
        element: <Calendar />,
      },
    ],
  },
] as RouteObject[])
