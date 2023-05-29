import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { DataService } from 'src/app/service/DataService';
import { Course } from '../models/course';

@Injectable({
  providedIn: 'root',
})
export class CourseService extends DataService<Course> {
  constructor(http: HttpClient) {
    super(http);
  }

  protected getUrl(): string {
    return 'http://localhost:8083/api/course';
  }
  getCourses(id: String[]): Observable<Course[]> {
    const url = `${this.getUrl()}/id`;
    return this.fetchData(url, id);
  }
  getCourse(id: string): Observable<Course> {
    const url = `${this.getUrl()}/id`;
    return this.getById(url, id);
  }
}
