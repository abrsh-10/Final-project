import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { CourseRoutingModule } from './course-routing.module';
import { CoursesComponent } from './courses/courses.component';
import { CourseComponent } from './course/course.component';
import { NavbarComponent } from '../navbar/navbar.component';

const routes = [{ path: '', component: CoursesComponent }];
@NgModule({
  declarations: [CoursesComponent, NavbarComponent, CourseComponent],
  imports: [CommonModule, CourseRoutingModule, RouterModule.forChild(routes)],
  exports: [CoursesComponent],
})
export class CourseModule {}
