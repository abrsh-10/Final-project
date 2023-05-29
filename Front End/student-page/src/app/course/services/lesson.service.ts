import { Injectable } from '@angular/core';
import { Lesson } from '../models/lesson';
import { HttpClient } from '@angular/common/http';
import { DataService } from 'src/app/service/DataService';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class LessonService extends DataService<Lesson> {
  constructor(http: HttpClient) {
    super(http);
  }

  protected getUrl(): string {
    return 'http://localhost:8083/api/lessons';
  }
  getLessons(id: any): Observable<Lesson> {
    const url = `${this.getUrl()}/topic-id`;
    return this.getById(url, id);
  }
}
