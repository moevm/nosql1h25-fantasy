# Build Angular-app
FROM node:20-alpine AS builder
WORKDIR /app

COPY package*.json ./
RUN npm install -g @angular/cli@17
RUN npm ci
COPY . .
RUN ng build

# Start nginx
FROM nginx:alpine
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf
EXPOSE 4200
CMD ["nginx", "-g", "daemon off;"]