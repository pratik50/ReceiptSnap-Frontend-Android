# ReceiptSnap – Android App 📱🧾


**ReceiptSnap** is a modern Android application that allows users to capture, upload, process, and manage receipts/documents with smart categorization using AWS services and scalable cloud infrastructure.

This repository contains the **Android frontend** of the ReceiptSnap project.  
The backend is developed separately and can be found here:  
👉 **ReceiptSnap Backend:** https://github.com/pratik50/ReceiptSnap-Backend


## ✨ Key Highlights

- 📸 Capture and upload receipt images  
- 🧾 Display and manage scanned receipts  
- ⚡ Smooth UI with loading animations  
- 🔍 Backend-ready for OCR & receipt extraction  
- 🧠 Built using modern Android architecture  


## 🏗 Architecture

The app follows **MVVM (Model–View–ViewModel)** architecture to ensure:

- Clear separation of concerns  
- Testable and maintainable code  
- Lifecycle-aware UI updates  

### Architectural Components

- MVVM  
- Repository Pattern  
- Dependency Injection with Hilt  

## 🧩 Tech Stack & Libraries 

### 🔹 Dependency Injection

- **Hilt (Dagger)** – Scalable dependency injection  
- **KSP** – Fast annotation processing  

### 🔹 Networking

- **Retrofit & OkHttp** – REST API communication  
- **Moshi** – JSON parsing  
- **Chucker** – API debugging (debug builds)  

### 🔹 Async & Background Work

- **Kotlin Coroutines** – Asynchronous and background tasks  

### 🔹 UI & UX

- Material Design Components  
- **Epoxy** – Declarative & efficient RecyclerView management  
- **Lottie** – Smooth animations  
- **Facebook Shimmer** – Loading placeholders  
- **Animated Bottom Bar** – Custom animated navigation  
- **SDP / SSP** – Responsive UI scaling  

## 📷 Screenshots / App Preview

<p align="center">
  <img src="https://github.com/user-attachments/assets/1e998779-dd95-4c52-8975-5fd1c8ba1750" width="20%" />
  <img src="https://github.com/user-attachments/assets/15e7c2fe-7303-4841-a326-6153ba8d090a" width="20%" />
  <img src="https://github.com/user-attachments/assets/f022e7d2-7dbd-4e39-a274-9797209e2566" width="20%" />
</p>
<p align="center">
  <img src="https://github.com/user-attachments/assets/234c41ab-648d-4ccd-b2ee-ed7879a8a6cf" width="20%" />
  <img src="https://github.com/user-attachments/assets/97a536a3-302b-4363-9553-fb9a13bfa4e5" width="20%" />
  <img src="https://github.com/user-attachments/assets/5aa9a82f-76ab-4d42-9d89-d3a5a4b7380c" width="20%" />
  <img src="https://github.com/user-attachments/assets/356adb79-86f9-4f53-8d55-6bc84078efaa" width="20%" />
</p>

## ⚙️ Setup & Installation
	
1.	Clone the repository:
```text

git clone https://github.com/pratik50/ReceiptSnap-Frontend-Android.git

```
2.	Open the project in Android Studio
3.	Sync Gradle and run on an emulator or physical device

## 🔗 Related Repositories

- **Frontend (Android App- this repo):** [ReceiptSnap-Frontend-Android](https://github.com/pratik50/ReceiptSnap-Frontend-Android)  
- **Backend (it's backend):** [ReceiptSnap-Backend](https://github.com/pratik50/ReceiptSnap-Backend)

## 🤝 Contributing
1.	Fork the repo
2.	Create a feature branch (git checkout -b feature-name)
3.	Commit changes (git commit -m "Added new feature")
4.	Push branch (git push origin feature-name)
5.	Open a Pull Request 🚀  

## 🆓 End Note  

This project is **open-source and free to use**.  
You are welcome to **modify, distribute, and learn** from it.  

Made with ❤️ by **Pratik Jadhav**  
