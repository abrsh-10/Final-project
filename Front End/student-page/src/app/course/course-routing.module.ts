import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CourseComponent } from './course/course.component';
import { CoursesComponent } from './courses/courses.component';

const routes: Routes = [
  { path: '', component: CoursesComponent },
  { path: 'course/:id', component: CourseComponent },
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
})
export class CourseRoutingModule {}
