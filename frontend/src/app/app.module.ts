import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { ReactiveFormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { LoginComponent } from './login/login.component';
// Dodaj tu inne komponenty jeśli masz

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    // Dodaj inne komponenty tutaj
  ],
  imports: [
    BrowserModule,
    FormsModule,             // ⬅️ do ngModel
    ReactiveFormsModule,     // ⬅️ do formControlName
    // Inne moduły jeśli potrzebne
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
