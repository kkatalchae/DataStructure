package stack;

import java.util.Arrays;
import java.util.EmptyStackException;

/*
    # Stack ( 스택 )

    스택은 한 쪽 끝에서만 자료를 넣거나 뺄 수 있는 선형 자료구조를 말한다.

    Last In First Out ( LIFO ) 가장 마지막에 넣은 것이 가장 먼저 나오는 구조를 가지고 있는 것이 특징이다.

    가장 먼저 넣은 것이 하단에 위치하고 다음 데이터를 추가할 때는 기존 구조에 데이터를 쌓는다고 하면 이해가 빠를 것이다.

    반대로 보관한 자료를 꺼낼 때는 가장 상단에 위치하는 것을 꺼낸다라고 생각하면 된다.

    흔히 사용하는 실행 취소 ( ctrl + z ) 도 가장 최근 실행한 명령어를 취소해야하기 때문에 스택 구조를 통해서 구현하는 것이 효율적이다.

    또한 프로세스를 구성하는 4 개의 요소 중 한 부분이며 함수의 호출에 관여한다.
 */

public class Stack < E > implements StackInterface < E >{

    private static final int DEFAULT_CAPACITY = 10; // 기본 크기
    private static final Object[] EMPTY_ARRAY = {}; // 빈 배열

    private Object[] array; // 데이터를 담을 배열
    private int size; // 담긴 데이터의 개수

    public Stack() { // 기본 빈 배열을 생성하는 생성자
        this.array = EMPTY_ARRAY;
        this.size = 0;
    }

    public Stack(int capacity) { // 입력받은 크기의 배열을 생성하는 생성자
        this.array = new Object[capacity];
        this.size = 0;
    }

    // 자료 구조는 기본적으로 동적 할당을 전제로 하기 때문에 이를 위한 resize 메소드
    private void resize() {

        // 빈 배열일 경우 기본 크기를 가지는 배열을 생성
        if (Arrays.equals(array, EMPTY_ARRAY)) {
            array = new Object[DEFAULT_CAPACITY];
            return;
        }

        int arrayCapacity = array.length;

        // 배열에 데이터가 가득찬 경우 크기를 늘려준다.
        if (size == arrayCapacity) {
            int newSize = arrayCapacity * 2;
            return;
        }

        // 배열에 데이터가 굉장히 적게 들어간 경우 크기를 줄여준다.
        if (size < (arrayCapacity / 2)) {
            int newCapacity = (arrayCapacity / 2);

            array = Arrays.copyOf(array, Math.max(DEFAULT_CAPACITY, newCapacity));
            return;
        }
    }


    // 스택에 데이터를 넣어주는 push 메소드
    @Override
    public E push(E item) {
        if (size == array.length) {
            resize();
        }

        // 배열의 가장 끝에 데이터를 넣어준다.
        array[size] = item;
        size++;
        return item;
    }


    // 스택의 가장 상단에 위치한 데이터를 확인하고 삭제하는 pop 메소드
    @SuppressWarnings("unchecked")
    @Override
    public E pop() {
        if (size == 0) { // 스택이 비어있는 경우 예외발생
            throw new EmptyStackException();
        }

        // 가장 상단에 데이터를 임시 변수에 담아둔다.
        E obj = (E) array[size - 1];

        // 가장 상단의 데이터를 없애준다.
        array[size - 1] = null;

        size--;
        resize();

        return obj;
    }

    // 스택의 가장 상단에 위치한 데이터를 조회하는 peek 메소드, pop 메소드와 달리 조회만 하고 삭제는 하지 않는다.
    @SuppressWarnings("unchecked")
    @Override
    public E peek() {
        if (size == 0) {
            throw new EmptyStackException();
        }

        return (E) array[size - 1];
    }

    // 스택에 입력받은 값이 존재하는지 확인하는 search 메소드, 스택이기 때문에 가장 상단부터 확인한다.
    @Override
    public int search(Object value) {
        for (int idx = size - 1; idx >= 0; idx--) {

            if (array[idx].equals(value)) {
                return size - idx;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++){
            array[i] = null;
        }
        size = 0;
        resize();
    }

    @Override
    public boolean empty(){
        return size == 0;
    }
}
