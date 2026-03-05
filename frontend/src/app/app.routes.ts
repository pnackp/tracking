import { Routes } from '@angular/router';
import { Login } from './login/login';
import { Register } from './register/register';
import { Notfoundpage } from './notfoundpage/notfoundpage';
import { Dashboard } from './dashboard/dashboard';
import { SensorManagementComponent } from './dashboard/sensor-management/sensor-management';
import { VerifyComponent } from './verify-component/verify-component';
import { WaitingVerify } from './waiting-verify/waiting-verify';
import { UsersManager } from './dashboard/users-manager/users-manager';

export const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  {
    path: 'login',
    component: Login,
    title: 'Tracking | Login'
  },
  {
    path: 'register',
    component: Register,
    title: 'Tracking | Register'
  },
  {
    path: 'dashboard',
    component: Dashboard,
    title: 'Tracking | Dashboard',
    children: [
      {
        path: 'sensormanage',
        component:SensorManagementComponent
      },
      {
        path: 'usersmanage',
        component : UsersManager
      }
    ]
  },
  {
    path: 'verify',
    component: VerifyComponent
  },
  {
    path: 'waiting',
    component: WaitingVerify
  },
  {
    path: '**',
    component: Notfoundpage,
    title: 'Tracking | 404'
  }
];