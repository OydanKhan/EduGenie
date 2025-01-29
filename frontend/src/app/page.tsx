"use client";

import React, { useState } from "react";
import Link from "next/link";
import styles from "./page.module.css";
import { Button } from "antd";
import { useRouter } from "next/navigation";
import SignInButton from "@/app/components/SignInButton";
import { User } from "@/types";

const HomePage: React.FC = () => {
  const router = useRouter();
  const [userInfo, setUserInfo] = useState<User | null>(null);

  const handleSignIn = (userInfo: User) => {
    setUserInfo(userInfo);
  };

  const handleClickSignIn = () => {
    setUserInfo(userInfo);
    location.href = "/course";
  };

  return (
    <div className={styles.mainBackground}>
      <div className={styles.mainT}>
        <h1 className={styles.mainPageTitle}>EduGenie</h1>
        <h2 className={styles.mainPageDesc}>
          AI-Powered Learning that Creates Custom Study Paths and Practice Exams
          for You.
        </h2>
        {userInfo ? (
          <Link href="/course">
            <Button type="primary" size="large">
              Get Started
            </Button>
          </Link>
        ) : (
          <SignInButton onAutoSignInSuccess={handleSignIn} onClickSignInSuccess={handleClickSignIn} size="large">
            Get Started
          </SignInButton>
        )}
      </div>
      <div className={styles.bgImageWrap}>
        <div className={styles.bgImage}></div>
      </div>
    </div>
  );
};

export default HomePage;
