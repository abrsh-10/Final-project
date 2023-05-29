import { Role } from './role';

export class User {
  id!: String;
  firstName!: String;
  lastName!: String;
  email!: String;
  role!: Role;
  isAllowed!: Boolean;
}
