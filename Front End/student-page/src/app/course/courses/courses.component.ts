import { Component, OnInit } from '@angular/core';
import { NavbarComponent } from 'src/app/navbar/navbar.component';
import { FooterComponent } from 'src/app/footer/footer.component';
import { CourseService } from '../services/course.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-courses',
  templateUrl: './courses.component.html',
  styleUrls: ['./courses.component.css'],
})
export class CoursesComponent implements OnInit {
  courses!: any[];
  courseIds: String[] = [
    '645fd39d922399163eaf8a94',
    '645fde09922399163eaf8a95',
    '645fde94922399163eaf8a96',
    '645fdfa3922399163eaf8a97',
    '645fe069922399163eaf8a98',
    '645fe0f6922399163eaf8a99',
  ];

  constructor(private courseService: CourseService, private router: Router) {}

  ngOnInit() {
    this.courseService.getCourses(this.courseIds).subscribe((data) => {
      this.courses = data;
    });
  }

  onSelectCourse(courseId: number) {
    this.router.navigate(['/course', courseId]);
  }
}
