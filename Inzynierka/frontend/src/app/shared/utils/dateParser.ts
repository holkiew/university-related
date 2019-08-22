import {Injectable} from "@angular/core";

@Injectable()
export class DatetimeLocalParser {
    public dateToString(date: Date): string {
        return (date.getFullYear().toString() + '-' + ("0" + (date.getMonth() + 1)).slice(-2) + '-' + ("0" + (date.getDate())).slice(-2))
            + 'T' + date.toTimeString().slice(0, 5);
    }

    public parseDate(date: string): Date {
        date = date.replace('T', '-');
        let parts = date.split('-');
        let timeParts = parts[3].split(':');
        // new Date(year, month [, day [, hours[, minutes[, seconds[, ms]]]]])
        return new Date(+parts[0], +parts[1] - 1, +parts[2], +timeParts[0], +timeParts[1]); // Note: months are 0-based
    }
}
