import { Injectable } from '@angular/core';
import { DataService } from '../../service/DataService';
import { CourseMaterial } from '../models/course-material';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CourseMaterialService extends DataService<CourseMaterial> {
  constructor(http: HttpClient) {
    super(http);
  }

  protected getUrl(): string {
    return 'http://localhost:8083/api/course-material';
  }

  getCourseMaterial(id: string): Observable<CourseMaterial> {
    const url = `${this.getUrl()}/id`;
    return this.getById(url, id);
  }
}
