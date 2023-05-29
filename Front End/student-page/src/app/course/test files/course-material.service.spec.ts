import { TestBed } from '@angular/core/testing';

import { CourseMaterialService } from '../services/course-material.service';

describe('CourseMaterialService', () => {
  let service: CourseMaterialService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CourseMaterialService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
