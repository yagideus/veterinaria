import { HttpClient} from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable} from 'rxjs';
import { User } from '../auth/user';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = `${environment.urlApi}user`;
  private apiUrl2 = `${environment.urlApi2}user`;

  constructor(private http:HttpClient) { }

  private getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? Number(userId) : null;
  }

  getUser(id:number):Observable<User>{
    return this.http.get<User>(environment.urlApi+"user/"+id)
  }

  updateUser(id: number, user: User): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, user);
  }

  
  getAllUsers(): Observable<User[]> {
    return this.http.get<User[]>(environment.urlApi+"user");
  }


  registerUser(user: User): Observable<any> {
    return this.http.post(environment.urlApi2 + 'register', user);
  }

  deleteUser(id: number): Observable<void> {
    return this.http.delete<void>(environment.urlApi + "user/" + id)
  }
 
   // Descargar el archivo Excel
   downloadExcel(): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/export.xlsx`, {
      responseType: 'blob'
    });
  }
}
