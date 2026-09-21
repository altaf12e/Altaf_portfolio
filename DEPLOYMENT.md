# 🚀 Portfolio Application Deployment Guide
### अपने Spring Boot Portfolio Website को Live Deploy करने की पूरी Guide (Hindi & English)

यह प्रोजेक्ट अब Production-Ready है। आप इसे **Render.com** (Recommended & Free), **Railway.app**, या किसी भी **Linux VPS / Docker** पर आसानी से 5 मिनट में लाइव होस्ट कर सकते हैं।

---

## 📌 Step 1: Code को GitHub पर Push करें (Mandatory First Step)

Cloud platforms (Render / Railway) आपके GitHub repository से ऑटोमैटिक कोड लेकर वेबसाइट को लाइव करते हैं।

अपने कंप्यूटर के Terminal / PowerShell में यह कमांड्स चलाएं:

```powershell
# 1. अपने प्रोजेक्ट फोल्डर में जाएं
cd C:\Users\Imtiyaz\OneDrive\Desktop\portfolio-springboot

# 2. Git initialize करें
git init

# 3. सभी फाइलों को स्टेज करें
git add .

# 4. First commit करें
git commit -m "Production deployment ready with certificates, resume viewer and docker setup"

# 5. Branch को main नाम दें
git branch -M main

# 6. GitHub पर नया Repository बनाएं (e.g. 'my-portfolio') और उसका URL जोड़ें:
git remote add origin https://github.com/<YOUR_GITHUB_USERNAME>/<YOUR_REPOSITORY_NAME>.git

# 7. Code push करें
git push -u origin main
```

---

## 🌐 Option 1: Render.com पर Free Deploy करें (सबसे आसान और Recommended ⭐)

Render.com पर Spring Boot Apps को free me host kiya ja sakta hai with **Free Automatic SSL Certificate (HTTPS)**.

### Step-by-Step Instructions:
1. **Account बनाएं**: [Render.com](https://render.com) पर जाएं और अपने **GitHub account** से Login / Sign Up करें।
2. **New Web Service**:
   - Dashboard पर ऊपर दाएँ कोने में **"New +"** बटन दबाएं।
   - **"Web Service"** चुनें।
3. **Repository Connect करें**:
   - "Build and deploy from a Git repository" चुनें।
   - अपना GitHub repository (`portfolio-springboot`) search karke **"Connect"** click karein.
4. **Settings Configure करें**:
   - **Name**: `altaf-portfolio` (या जो नाम आप चाहें, URL बनेगा: `https://altaf-portfolio.onrender.com`)
   - **Language / Runtime**: **Docker** चुनें (Render ऑटोमैटिकली `Dockerfile` detect kar lega)
   - **Region**: Singapore / Oregon / Frankfurt (Koi bhi chunein)
   - **Branch**: `main`
   - **Instance Type**: **Free**
5. **Environment Variables (Optional, for Custom Credentials)**:
   - "Advanced" -> "Add Environment Variable" par click karein:
     - `PORT` = `10000`
     - `ADMIN_USERNAME` = `admin`
     - `ADMIN_PASSWORD` = `AapkaSecretPassword123`
6. **Deploy**:
   - **"Create Web Service"** button dabayein!
   - Render 2-3 minute me project compile karega aur aapko ek live public link mil jayega:
     👉 **`https://altaf-portfolio.onrender.com`**

> **Note**: Jab bhi aap GitHub par naya code push karenge, Render automatically use re-build karke live site ko update kar dega!

---

## 🚂 Option 2: Railway.app पर Deploy करें

Railway par bhi GitHub connect karke 1-click me deploy hota hai:
1. [Railway.app](https://railway.app) par jayein aur GitHub se Login karein.
2. **"New Project"** -> **"Deploy from GitHub repo"** select karein.
3. Apna repository select karein.
4. Railway automatic `Dockerfile` detect karke deploy start kar dega.
5. "Settings" me jaakar **"Generate Domain"** click karein (e.g. `altaf-portfolio.up.railway.app`).

---

## 🐳 Option 3: Docker Compose (VPS / Local Server)

Agar aapke paas VPS (Hostinger, AWS EC2, DigitalOcean, Linode) hai:

```bash
# Docker compose chala kar background me start karein
docker compose up -d --build

# Server status check karein
docker compose ps

# Logs check karein
docker compose logs -f
```

Website `http://YOUR_SERVER_IP:8080` par live ho jayegi.

---

## 🔒 Production Credentials & Security

Production me default credentials badalna na bhoolein:
- **Default Admin Login**:
  - URL: `https://your-live-url/admin/login`
  - User: `admin`
  - Pass: `admin123`
- Isko change karne ke liye cloud provider (Render / Railway) ke **Environment Variables** me `ADMIN_USERNAME` aur `ADMIN_PASSWORD` set karein.

---

## 📁 Important Production Files Included

| File | Purpose |
|---|---|
| `Dockerfile` | Multi-stage lightweight production container (JDK 17 + JRE 17 Alpine) |
| `docker-compose.yml` | 1-command startup with persistent database volume |
| `render.yaml` | Render.com Infrastructure-as-Code blueprint |
| `Procfile` | Cloud PaaS runtime configuration |
| `.gitignore` | Prevents cache, compiled classes, and DB locks from being uploaded |
| `application.properties` | Configured with dynamic `${PORT}` and environment variables |
