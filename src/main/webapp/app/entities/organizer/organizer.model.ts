import { BaseEntity } from './../../shared';

export class Organizer implements BaseEntity {
    constructor(
        public id?: number,
        public name?: string,
        public email?: string,
        public markeds?: BaseEntity[],
    ) {
    }
}
