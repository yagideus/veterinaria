import { jwtDecode } from "jwt-decode";

export function getUserRoleFromToken(token: string): string | null {
  try {
    const decoded: any = jwtDecode(token);
    return decoded.role && decoded.role[0] ? decoded.role[0] : null;
  } catch (error) {
    return null;
  }
}