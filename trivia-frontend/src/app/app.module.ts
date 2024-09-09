import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';

import { AppComponent } from './app.component';
import { TriviaComponent } from './trivia/trivia.component';
import { FormsModule } from '@angular/forms';
import { TriviaService } from "./services/TriviaService";
import { RouterOutlet } from "@angular/router";
import { MatCardModule } from "@angular/material/card";
import { MatRadioModule } from "@angular/material/radio";
import { MatInputModule} from "@angular/material/input";
import { MatSelectModule } from "@angular/material/select";
import { MatFormFieldModule } from "@angular/material/form-field";
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { MatButtonModule } from "@angular/material/button";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";

@NgModule({
  declarations: [
    AppComponent,
    TriviaComponent
  ],
  imports: [
    RouterOutlet,
    BrowserModule,
    HttpClientModule,
    FormsModule,
    MatCardModule,
    MatRadioModule,
    MatSelectModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    BrowserAnimationsModule,
    NgbModule
  ],
  providers: [TriviaService],
  bootstrap: [AppComponent]
})
export class AppModule { }
