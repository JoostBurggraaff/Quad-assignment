import { Component, OnInit } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';
import { TriviaService } from "../services/TriviaService";

@Component({
  selector: 'app-trivia',
  templateUrl: './trivia.component.html',
  styleUrls: ['./trivia.component.css']
})
export class TriviaComponent implements OnInit {
  userId: string;
  questions: any[] = [];
  userAnswers: any[] = [];
  result: any = null;
  options = {
    amount: 1,
    category: '',
    difficulty: '',
    type: ''
  };
  categories: any[] = [{ id: '0', name: 'Any'}];
  difficulties = ['any', 'easy', 'medium', 'hard'];

  constructor(private triviaService: TriviaService, private sanitizer: DomSanitizer) {
    this.userId = this.getUserId();
  }

  ngOnInit(): void {
    this.loadCategories();
    this.loadQuestions();
  }

  getUserId(): string {
    let userId = localStorage.getItem('userId');
    if (!userId) {
      userId = Math.random().toString(36).substr(2, 9);
      localStorage.setItem('userId', userId);
    }
    return userId;
  }

  loadCategories(): void {
    this.triviaService.getCategories().subscribe((data: any) => {
      this.categories = [{ id: '0', name: 'Any' }, ...data.trivia_categories];
    });
  }

  loadQuestions(): void {
    const { amount, category, difficulty, type } = this.options;
    this.triviaService.getQuestions(this.userId, category, difficulty, amount, type).subscribe((data: any) => {
      this.questions = data.map((q: any) => {
        return {
          plainQuestion: q.question,
          sanitizedQuestion: this.sanitizer.bypassSecurityTrustHtml(q.question),
          options: q.options.map((opt: string) => ({
            plainOption: opt,
            sanitizedOption: this.sanitizer.bypassSecurityTrustHtml(opt)
          }))
        };
      });
      this.userAnswers = this.questions.map((q: any) => ({ question: q.plainQuestion, answer: '' }));
      this.result = null;
    });
  }


  selectAnswer(question: string, answer: string): void {
    const userAnswer = this.userAnswers.find(a => a.question === question);
    if (userAnswer) {
      userAnswer.answer = answer;
    }
  }

  submitAnswers(): void {
    const formattedAnswers = this.userAnswers.map(ans => ({
      question: ans.question,
      answer: ans.answer
    }));

    this.triviaService.checkAnswers(formattedAnswers).subscribe(result => {
      this.result = result;
    });
  }

}
