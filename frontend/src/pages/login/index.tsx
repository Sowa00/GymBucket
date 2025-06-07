import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useForm } from 'react-hook-form'

interface ILogin {
  email: string
  password: string
}

function LoginPage() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const navigate = useNavigate()

  const { register, handleSubmit } = useForm()

  const handleLogin = (data: ILogin) => {
    console.log(data)
    if (data.email === 'a@a.com' && data.password === '2137') {
      navigate('/homepage')
    } else {
      alert('co')
    }
  }
  console.log(register(email))
  return (
    <form onSubmit={handleSubmit(handleLogin)}>
      <h2>Logowanie</h2>
      <div>
        <input {...register('email')} />
      </div>

      <div>
        <input
          type='password'
          {...register('password')}
        />
      </div>

      <button type='submit'>Zaloguj</button>
    </form>
  )
}

export default LoginPage
