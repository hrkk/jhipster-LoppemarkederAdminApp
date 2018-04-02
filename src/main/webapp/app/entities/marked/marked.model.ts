import { BaseEntity } from './../../shared';

export class Marked implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public markedInformation?: string,
        public markedRules?: string,
        public entreInfo?: string,
        public dateExtraInfo?: string,
        public address?: string,
        public fromDate?: any,
        public toDate?: any,
        public latitude?: number,
        public longitude?: number,
        public enableBooking?: boolean,
        public organizer?: BaseEntity,
    ) {
        this.enableBooking = false;
    }
}
