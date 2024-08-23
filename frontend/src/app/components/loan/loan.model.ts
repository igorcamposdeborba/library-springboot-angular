export interface Loan {
    bookId: number;
    userId: number;
    bookTitle: string;
    book: {
        id: number;
        title: string;
        author: string;
        yearPublication: string;
        price: number;
    }
    users: {
            id: number;
            name: string;
            email: string;
            active: boolean,
            department: string;
            specialty: string;
        },
        effectiveFrom: string;
        effectiveTo: string;
        delivered: boolean;
}