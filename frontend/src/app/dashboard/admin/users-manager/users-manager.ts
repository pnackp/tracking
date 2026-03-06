import { Component, OnInit, signal } from '@angular/core';
import { ApiComponent } from '../../../api.component';
import { Employee } from '../../../app.dto';
import { DatePipe, NgClass, TitleCasePipe } from '@angular/common';
import { FormControl, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { EmployeeUpdateAdmin } from '../../../app.dto';

@Component({
  selector: 'app-users-manager',
  standalone: true,
  imports: [DatePipe, NgClass, TitleCasePipe, ReactiveFormsModule],
  templateUrl: './users-manager.html',
  styleUrl: './users-manager.css',
})

export class UsersManager implements OnInit {
  constructor(private api: ApiComponent) { }

  originalEmployee = signal<Employee[]>([]);
  ListEmployee = signal<Employee[]>([]);
  selectedUser = signal<Employee | null>(null);
  onDelete = signal(false);

  searchForm = new FormGroup({
    findBy: new FormControl(""),
    Role: new FormControl(""),
    Status: new FormControl("")
  })

  editForm = new FormGroup({
    role: new FormControl(''),
    isActive: new FormControl(true)
  });

  ngOnInit(): void {
    this.CallList();
  }

  CallList() {
    this.api.GetEmployees().subscribe({
      next: (res) => {
        this.originalEmployee.set(res);
        this.ListEmployee.set(res);
      }
    });
  }

  isEdit = signal(false);

  OnEdit(user: Employee) {
    this.selectedUser.set(user);

    this.editForm.patchValue({
      role: user.role,
      isActive: user.isActive
    });

    this.isEdit.set(true);
  }

  OnSave() {
    const user = this.selectedUser();
    if (!user) return;

    const formValue = this.editForm.value;

    const payload: EmployeeUpdateAdmin = {
      is_active: formValue.isActive!,
      role: formValue.role!
    };

    this.api.PutEmployees(payload, Number(user.employeeId)).subscribe({
      next: () => {

        this.ListEmployee.update(list =>
          list.map(s =>
            s.employeeId === user.employeeId
              ? {
                ...s,
                isActive: formValue.isActive!,
                role: formValue.role!
              }
              : s
          )
        );
        this.originalEmployee.update(list =>
          list.map(s =>
            s.employeeId === user.employeeId
              ? {
                ...s,
                isActive: formValue.isActive!,
                role: formValue.role!
              }
              : s
          )
        );
        this.isEdit.set(false);
      },
      error: err => {
        console.error(err);
      }
    });
  }

  CloseEdit() {
    this.isEdit.set(false);
    this.selectedUser.set(null);
  }

  OnSearch() {

    const { findBy, Role, Status } = this.searchForm.value;
    let data = this.originalEmployee();
    if (findBy && findBy.trim() !== "") {
      const search = findBy.toLowerCase();

      data = data.filter(emp =>
        emp.employeeId.toLowerCase().includes(search) ||
        emp.firstName.toLowerCase().includes(search) ||
        emp.lastName.toLowerCase().includes(search)
      );
    }
    if (Role && Role !== "") {
      data = data.filter(emp => emp.role === Role);
    }

    if (Status && Status !== "") {
      const isActive = Status === "active";
      data = data.filter(emp => emp.isActive === isActive);
    }

    this.ListEmployee.set(data);
  }

  OnReset() {
    this.searchForm.reset({
      findBy: '',
      Role: '',
      Status: ''
    });

    this.ListEmployee.set(this.originalEmployee());
  }

  OnDelete(user: Employee) {
    this.onDelete.set(true)
    this.selectedUser.set(user);
  }

  ConfirmDelete() {
    const user = this.selectedUser();
    if (!user) return;

    this.api.DeleteEmployees(Number(user.employeeId)).subscribe({
      next: () => {
        this.originalEmployee.update(list =>
          list.filter(emp => emp.employeeId !== user.employeeId)
        );
        this.ListEmployee.update(list =>
          list.filter(emp => emp.employeeId !== user.employeeId)
        );
        this.onDelete.set(false);
        this.selectedUser.set(null);
      },
      error: err => {
        console.error(err);
      }
    });
  }

  CloseDelete() {
    this.onDelete.set(false);
    this.selectedUser.set(null);
  }
}