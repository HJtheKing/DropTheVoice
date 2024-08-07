# Step 1: Build Stage
FROM node:16 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Step 2: Production Stage
FROM nginx:1.21.6
COPY --from=build-stage /app/dist /usr/share/nginx/html
#COPY ./nginx.conf /etc/nginx/nginx.conf
#COPY ./localhost.key /etc/nginx/ssl/localhost.key
#COPY ./localhost.crt /etc/nginx/ssl/localhost.crt
COPY ./privatekey.pem /etc/nginx/ssl/privatekey.pem
COPY ./fullchain.pem /etc/nginx/ssl/fullchain.pem
COPY ./nginx-prod.conf /etc/nginx/nginx.conf

EXPOSE 80
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]