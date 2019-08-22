/**
 * Created by DZONI on 21.11.2016.
 */
export interface Auction {
    id: number;
    title: string;
    dateStart: Date;
    dateEnd: Date;
    premium: boolean;
    ended: boolean;
}
