import { Injectable } from '@angular/core';
import { DataService } from 'src/app/service/DataService';
import { Topic } from '../models/topic';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class TopicService extends DataService<Topic> {
  constructor(http: HttpClient) {
    super(http);
  }
  protected getUrl(): string {
    return 'http://localhost:8083/api/topic';
  }
  getTopics(id: String): Observable<Topic> {
    const url = `${this.getUrl()}/id`;
    return this.getById(url, id);
  }
}
