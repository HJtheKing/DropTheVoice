# Step 1: Build Stage
FROM node:16 AS build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Step 2: Production Stage
FROM nginx:1.21.6-alpine
COPY --from=build-stage /app/dist /usr/share/nginx/html
#COPY ./nginx.conf /etc/nginx/nginx.conf
#COPY ./localhost.key /etc/nginx/ssl/localhost.key
#COPY ./localhost.crt /etc/nginx/ssl/localhost.crt
COPY ./i11a505.p.ssafy.io.key /etc/nginx/ssl/i11a505.p.ssafy.io.key
COPY ./i11a505.p.ssafy.io.crt /etc/nginx/ssl/i11a505.p.ssafy.io.crt
COPY ./nginx-prod.conf /etc/nginx/nginx.conf

EXPOSE 80
EXPOSE 443
CMD ["nginx", "-g", "daemon off;"]