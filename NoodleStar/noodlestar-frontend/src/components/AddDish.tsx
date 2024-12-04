import * as React from 'react';
import { FormEvent, useState, ChangeEvent } from 'react';
import { Button, Modal, Form } from 'react-bootstrap';
import { Status } from '../features/model/Status.ts';
import { addDish } from '../features/api/addDish.ts';
import { menuRequestModel } from '../features/model/menuRequestModel.ts';
import { useNavigate } from 'react-router-dom';

// Define AddDishProps interface to include closeModal
interface AddDishProps {
    closeModal: () => void;
}

const AddDish: React.FC<AddDishProps> = ({ closeModal }): JSX.Element => {
    const navigate = useNavigate();
    const [dish, setDish] = useState<menuRequestModel>({
        name: '',
        description: '',
        price: 0,
        category: '',
        itemImage: '',
        status: Status.AVAILABLE,  // Default status is set to AVAILABLE
    });
    const [errors, setErrors] = useState<{ [key: string]: string }>({});
    const [show, setShow] = useState(false);

    const handleSubmit = async (event: FormEvent<HTMLFormElement>): Promise<void> => {
        event.preventDefault();
        if (!validate()) return;

        const dishPayload = {
            name: dish.name,
            description: dish.description,
            price: dish.price,
            category: dish.category,
            itemImage: dish.itemImage,
            status: dish.status,
        };

        try {
            const response = await addDish(dishPayload);
            if (response.status === 201) {
                closeModal(); // Close the modal after a successful dish addition
                navigate('/menu');
                window.location.reload();
            } else {
                console.error('Failed to add dish');
            }
        } catch (error) {
            console.error('Error adding dish:', error);
        }
    };

    const handleChange = (e: ChangeEvent<HTMLInputElement | HTMLTextAreaElement>): void => {
        const { name, value } = e.target;
        setDish({ ...dish, [name]: value });
    };

    const validate = (): boolean => {
        const newErrors: { [key: string]: string } = {};
        if (!dish.name) newErrors.name = 'Dish name is required';
        if (!dish.description) newErrors.description = 'Description is required';
        if (!dish.price || dish.price <= 0) newErrors.price = 'Price must be a positive number';
        if (!dish.category) newErrors.category = 'Category is required';
        setErrors(newErrors);
        return Object.keys(newErrors).length === 0;
    };

    return (
        <>
            <Button variant="primary" onClick={() => setShow(true)}>
                Add Dish
            </Button>

            <Modal show={show} onHide={closeModal} backdrop="static" keyboard={false}>
                <Modal.Header closeButton>
                    <Modal.Title>Add New Dish</Modal.Title>
                </Modal.Header>

                <Modal.Body>
                    <Form id="addDishForm" onSubmit={handleSubmit}>
                        <Form.Group className="mb-3">
                            <Form.Label>Dish Name</Form.Label>
                            <Form.Control
                                type="text"
                                name="name"
                                value={dish.name}
                                onChange={handleChange}
                                isInvalid={!!errors.name}
                            />
                            <Form.Control.Feedback type="invalid">{errors.name}</Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Description</Form.Label>
                            <Form.Control
                                as="textarea"
                                name="description"
                                value={dish.description}
                                onChange={handleChange}
                                isInvalid={!!errors.description}
                            />
                            <Form.Control.Feedback type="invalid">{errors.description}</Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Price</Form.Label>
                            <Form.Control
                                type="number"
                                name="price"
                                value={dish.price}
                                onChange={handleChange}
                                isInvalid={!!errors.price}
                            />
                            <Form.Control.Feedback type="invalid">{errors.price}</Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Category</Form.Label>
                            <Form.Control
                                type="text"
                                name="category"
                                value={dish.category}
                                onChange={handleChange}
                                isInvalid={!!errors.category}
                            />
                            <Form.Control.Feedback type="invalid">{errors.category}</Form.Control.Feedback>
                        </Form.Group>

                        <Form.Group className="mb-3">
                            <Form.Label>Image URL</Form.Label>
                            <Form.Control
                                type="text"
                                name="itemImage"
                                value={dish.itemImage}
                                onChange={handleChange}
                            />
                        </Form.Group>
                    </Form>
                </Modal.Body>

                <Modal.Footer>
                    <Button variant="secondary" onClick={closeModal}>
                        Close
                    </Button>
                    <Button form="addDishForm" variant="primary" type="submit">
                        Add Dish
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    );
};

export default AddDish;
