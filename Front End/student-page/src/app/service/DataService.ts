import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, forkJoin } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class DataService<T> {
  constructor(private http: HttpClient) {}

  getAll(url: string): Observable<T[]> {
    return this.http.get<T[]>(url);
  }

  getById(url: string, id: String): Observable<T> {
    const apiUrl = `${url}/${id}`;
    return this.http.get<T>(apiUrl);
  }
  fetchData(apiUrl: String, ids: String[]): Observable<any[]> {
    const requests = [];

    for (const id of ids) {
      const url = `${apiUrl}/${id}`;
      requests.push(this.http.get<any>(url));
    }

    return forkJoin(requests);
  }

  add(url: string, item: T): Observable<T> {
    return this.http.post<T>(url, item);
  }

  update(url: string, id: String, item: T): Observable<T> {
    const apiUrl = `${url}/${id}`;
    return this.http.put<T>(apiUrl, item);
  }

  delete(url: string, id: String): Observable<T> {
    const apiUrl = `${url}/${id}`;
    return this.http.delete<T>(apiUrl);
  }
  downloadFile(id: string) {
    const url = `http://localhost:8084/file/download/${id}`; // replace with your backend API endpoint
    return this.http.get(url, { responseType: 'blob' });
  }
}
