import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriviaService {
  private categoriesUrl = 'https://opentdb.com/api_category.php';
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  getQuestions(userId: string, category?: string, difficulty?: string, amount: number = 5, type: string = 'multiple'): Observable<any> {
    let params = `?userId=${userId}&amount=${amount}&type=${type}`;
    if (category) {
      params += `&category=${category}`;
    }
    if (difficulty) {
      params += `&difficulty=${difficulty}`;
    }
    return this.http.get(`${this.apiUrl}/questions${params}`);
  }

  checkAnswers(answers: any[]): Observable<any> {
    return this.http.post(`${this.apiUrl}/checkanswers`, answers);
  }

  getCategories(): Observable<any> {
    return this.http.get(this.categoriesUrl);
  }
}
