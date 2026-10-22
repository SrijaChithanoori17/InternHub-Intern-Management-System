
import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Batch {
  id: number;
  startDate: string;
  endDate: string;
  totalInterns: number;
}

export interface Intern {
  // Numeric database ID used by PUT and DELETE endpoints.
  id: number;

  // Generated business ID, for example EMP20261005-001.
  internId: string;

  name: string;
  email: string;
  mobileNumber: string;
  idCardType: 'FREE' | 'PREMIUM';
  dateOfJoining: string;
  batchId: number;
  batchStartDate: string;
  batchEndDate: string;
}

export interface CreateInternRequest {
  name: string;
  email: string;
  mobileNumber: string;
  idCardType: 'FREE' | 'PREMIUM';
  dateOfJoining: string;
  batchId: number;
}

export interface UpdateInternRequest {
  name: string;
  email: string;
  mobileNumber: string;
}

export interface BatchDetails {
  id: number;
  startDate: string;
  endDate: string;
  totalInterns: number;
  interns: Intern[];
}

@Injectable({
  providedIn: 'root',
})
export class InternService {
  private readonly apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) {}

  // =========================================
  // INTERNS
  // =========================================

  getInterns(): Observable<Intern[]> {
    return this.http.get<Intern[]>(
      `${this.apiUrl}/interns`,
    );
  }

  registerIntern(
    request: CreateInternRequest,
  ): Observable<Intern> {
    return this.http.post<Intern>(
      `${this.apiUrl}/interns`,
      request,
    );
  }

  updateIntern(
    id: number,
    request: UpdateInternRequest,
  ): Observable<Intern> {
    return this.http.put<Intern>(
      `${this.apiUrl}/interns/${id}`,
      request,
    );
  }

  deleteIntern(id: number): Observable<void> {
    return this.http.delete<void>(
      `${this.apiUrl}/interns/${id}`,
    );
  }

  searchInterns(
    name?: string,
    batchId?: number,
    idCardType?: string,
  ): Observable<Intern[]> {
    let params = new HttpParams();

    if (name) {
      params = params.set('name', name);
    }

    if (batchId !== undefined) {
      params = params.set('batchId', batchId);
    }

    if (idCardType) {
      params = params.set('idCardType', idCardType);
    }

    return this.http.get<Intern[]>(
      `${this.apiUrl}/interns`,
      { params },
    );
  }

  // =========================================
  // BATCHES
  // =========================================

  getBatches(): Observable<Batch[]> {
    return this.http.get<Batch[]>(
      `${this.apiUrl}/batches`,
    );
  }

  createBatch(
    batch: { startDate: string },
  ): Observable<Batch> {
    return this.http.post<Batch>(
      `${this.apiUrl}/batches`,
      batch,
    );
  }

  getBatchDetails(id: number): Observable<BatchDetails> {
    return this.http.get<BatchDetails>(
      `${this.apiUrl}/batches/${id}`,
    );
  }
}
