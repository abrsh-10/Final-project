import { Component, OnInit } from '@angular/core';
import { User } from 'src/User/user';
import { UserService } from 'src/User/user.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css'],
})
export class NavbarComponent implements OnInit {
  user!: User;
  constructor(private userService: UserService) {}

  ngOnInit() {
    const email = 'abrhamsisay33@gmail.com';
    this.userService.getByEmail(email).subscribe((user) => (this.user = user));
  }
}
