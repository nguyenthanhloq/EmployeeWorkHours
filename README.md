# Employee Working Hours and Salary Management (Android, Java, Room)

## Demo accounts (seeded on first launch)
| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Employee | emp1 | emp123 |

## Features
- Login with Admin / Employee roles; session kept in SharedPreferences
- Room (SQLite) tables: Account, Employee, WorkLog, Salary
- Admin: add / update / delete / search / view employees (with hourly rate), calculate and manage monthly salaries (tap = toggle PAID, long-press = delete)
- Employee: daily check-in / check-out, attendance history, monthly hours / overtime / salary

## Business rules
- Hours per day = check-out - check-in; hours over 8 per day are overtime paid at 1.5x (`Utils.STANDARD_HOURS_PER_DAY`, `Utils.OVERTIME_RATE`)
- Salary = (total - overtime) x rate + overtime x rate x 1.5; PAID records are never recalculated
- Validation: one check-in per day, no check-out without check-in, check-out must be after check-in; a check-in with no check-out by the next day becomes INCOMPLETE and is excluded from salary

## Run
Open the folder in Android Studio, let Gradle sync, run on an emulator (API 24+).

## Git
```
git init
git add .
git commit -m "Initial project"
git branch -M main
git remote add origin <your-repo-url>
git push -u origin main
```
