<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    xmlns:tool="http://schemas.android.com/tools">

    <LinearLayout
        android:id="@+id/toolbarLayout"
        android:layout_width="0dp"
        android:layout_height="?actionBarSize"
        android:layout_marginTop="4dp"
        android:gravity="center_vertical"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent">

        <TextView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginStart="16dp"
            android:text="판매 글 보기"
            android:textColor="@color/black"
            android:textSize="20sp"
            android:textStyle="bold" />
    </LinearLayout>

    <View
        android:id="@+id/toolbarUnderLineView"
        android:layout_width="0dp"
        android:layout_height="1dp"
        android:background="@color/gray_cc"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/toolbarLayout" />

    <TextView
        android:id="@+id/titleText"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="20dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/radioStatus"
        android:textSize="18sp" />

    <TextView
        android:id="@+id/priceText"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="20dp"
        android:inputType="numberDecimal"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/titleText"
        android:textSize="18sp" />

    <TextView
        android:id="@+id/seller"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="20dp"
        android:backgroundTint="@color/hansung"
        android:textSize="18sp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/priceText" />

    <TextView
        android:id="@+id/content"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginTop="20dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/seller"
        android:textSize="18sp" />

    <!--    Button은 theme에 의해 배경색이 고정된 컴포넌트이므로 배경색을 바꾸려면 background가 아니라 backgroundTint를 바꿔줘야 적용됨-->
    <ImageView
        android:id="@+id/photoImageView"
        android:layout_width="250dp"
        android:layout_height="250dp"
        android:layout_marginTop="20dp"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.496"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/toolbarLayout" />

    <Button
        android:id="@+id/chatButton"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_marginHorizontal="16dp"
        android:layout_marginBottom="16dp"
        android:backgroundTint="@color/hansung"
        android:text="채팅하기"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent" />

    <ProgressBar
        android:id="@+id/progressBar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:visibility="gone"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.498"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.254"
        tool:visibility="visible" />

    <RadioButton
        android:id="@+id/radioStatus"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_marginStart="16dp"
        android:layout_marginTop="20dp"
        android:clickable="false"
        android:enabled="false"
        android:text="판매완료"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toBottomOf="@+id/photoImageView" />


</androidx.constraintlayout.widget.ConstraintLayout>
