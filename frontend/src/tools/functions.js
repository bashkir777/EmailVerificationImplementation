import validator from 'email-validator';

export function validateEmail(email) {
    return validator.validate(email);
}


export const asyncPostRequest = async (body, URL) => fetch(URL, {
    method: "POST",
    body: JSON.stringify({
        ...body,
    }),
    headers: {
        "Content-Type": "application/json"
    }
})