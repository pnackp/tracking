import { Routes } from '@angular/router';
import { LoginComponent } from './login/login';
import { RegisterComponent } from './register/register';
import { Dashboard } from './dashboard/dashboard';

export const routes: Routes = [
    {path : '' , pathMatch:'full' , redirectTo: 'login'},
    {path : 'login' , component:LoginComponent , title:'TrackSystem • Login'},
    {path : 'register' , component:RegisterComponent , title: 'TrackSystem • Register'},
    {path : 'dashboard' , component:Dashboard , title: 'TrackSystem • Dashboard'}
];
